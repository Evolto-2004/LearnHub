package xyz.learnhub.api.checks;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import xyz.learnhub.common.constant.BackendConstant;
import xyz.learnhub.common.domain.AdminUser;
import xyz.learnhub.common.domain.Department;
import xyz.learnhub.common.domain.User;
import xyz.learnhub.common.exception.NotFoundException;
import xyz.learnhub.common.service.AdminUserService;
import xyz.learnhub.common.service.DepartmentService;
import xyz.learnhub.common.service.UserService;
import xyz.learnhub.course.domain.Course;
import xyz.learnhub.course.domain.CourseAttachment;
import xyz.learnhub.course.domain.CourseChapter;
import xyz.learnhub.course.domain.CourseHour;
import xyz.learnhub.course.domain.UserLearnDurationStats;
import xyz.learnhub.course.service.CourseAttachmentService;
import xyz.learnhub.course.service.CourseChapterService;
import xyz.learnhub.course.service.CourseHourService;
import xyz.learnhub.course.service.CourseService;
import xyz.learnhub.course.service.UserCourseHourRecordService;
import xyz.learnhub.course.service.UserCourseRecordService;
import xyz.learnhub.course.service.UserLearnDurationStatsService;
import xyz.learnhub.resource.domain.Resource;
import xyz.learnhub.resource.domain.ResourceExtra;
import xyz.learnhub.resource.service.ResourceExtraService;
import xyz.learnhub.resource.service.ResourceService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Order(110)
@Slf4j
public class DemoDataCheck implements CommandLineRunner {

    private static final String DEFAULT_ADMIN_EMAIL = "admin@learnhub.local";
    private static final String DEMO_PASSWORD = "learnhub";

    @Autowired private AdminUserService adminUserService;

    @Autowired private DepartmentService departmentService;

    @Autowired private UserService userService;

    @Autowired private ResourceService resourceService;

    @Autowired private ResourceExtraService resourceExtraService;

    @Autowired private CourseService courseService;

    @Autowired private CourseChapterService chapterService;

    @Autowired private CourseHourService hourService;

    @Autowired private CourseAttachmentService attachmentService;

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private UserLearnDurationStatsService userLearnDurationStatsService;

    @Value("${learnhub.demo-data.enabled:false}")
    private Boolean enabled;

    @Override
    @Transactional
    public void run(String... args) {
        if (!Boolean.TRUE.equals(enabled)) {
            return;
        }

        try {
            seed();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("Demo data initialization failed: {}", e.getMessage());
        }
    }

    private void seed() throws NotFoundException {
        Integer adminId = defaultAdminId();
        DemoDepartments departments = seedDepartments();
        DemoResources resources = seedResources(adminId);
        List<User> users = seedUsers(departments);
        List<CourseSeed> courses = seedCourses(adminId, departments, resources);

        seedLearningRecords(users, courses);
        seedLearningStats(users);
    }

    private Integer defaultAdminId() {
        AdminUser adminUser = adminUserService.findByEmail(DEFAULT_ADMIN_EMAIL);
        return adminUser == null ? 0 : adminUser.getId();
    }

    private DemoDepartments seedDepartments() throws NotFoundException {
        Integer university = ensureDepartment("LearnHub University", "LearnHub Academy", 0, 10);
        Integer computerScience =
                ensureDepartment("School of Computer Science", "Product", university, 10);
        Integer softwareEngineering =
                ensureDepartment("School of Software Engineering", "Engineering", university, 20);
        Integer business =
                ensureDepartment("School of Business", "Sales", university, 30);
        Integer digitalMedia =
                ensureDepartment("School of Digital Media", "Customer Success", university, 40);
        Integer generalEducation =
                ensureDepartment("General Education Center", "People Ops", university, 50);

        return new DemoDepartments(
                computerScience, softwareEngineering, business, digitalMedia, generalEducation);
    }

    private List<User> seedUsers(DemoDepartments departments) {
        List<DemoUser> demoUsers =
                List.of(
                        new DemoUser(
                                "ming.li@univ.local",
                                "ava.chen@learnhub.local",
                                "Ming Li",
                                departments.softwareEngineering(),
                                recentMinutesAgo(15),
                                18),
                        new DemoUser(
                                "yue.zhang@univ.local",
                                "ben.carter@learnhub.local",
                                "Yue Zhang",
                                departments.computerScience(),
                                recentMinutesAgo(30),
                                16),
                        new DemoUser(
                                "hao.wang@univ.local",
                                "chloe.li@learnhub.local",
                                "Hao Wang",
                                departments.business(),
                                recentMinutesAgo(45),
                                21),
                        new DemoUser(
                                "xinyi.chen@univ.local",
                                "daniel.kim@learnhub.local",
                                "Xinyi Chen",
                                departments.digitalMedia(),
                                recentMinutesAgo(60),
                                12),
                        new DemoUser(
                                "jiayi.liu@univ.local",
                                "ella.wong@learnhub.local",
                                "Jiayi Liu",
                                departments.generalEducation(),
                                yesterdayAt(10),
                                15),
                        new DemoUser(
                                "chen.yang@univ.local",
                                "felix.zhou@learnhub.local",
                                "Chen Yang",
                                departments.softwareEngineering(),
                                yesterdayAt(11),
                                20),
                        new DemoUser(
                                "ruohan.zhao@univ.local",
                                "grace.lin@learnhub.local",
                                "Ruohan Zhao",
                                departments.computerScience(),
                                daysAgoAt(4, 9),
                                24),
                        new DemoUser(
                                "zihan.sun@univ.local",
                                "henry.yang@learnhub.local",
                                "Zihan Sun",
                                departments.business(),
                                daysAgoAt(5, 14),
                                11),
                        new DemoUser(
                                "yilin.ma@univ.local",
                                "iris.ma@learnhub.local",
                                "Yilin Ma",
                                departments.digitalMedia(),
                                daysAgoAt(6, 15),
                                17),
                        new DemoUser(
                                "boyuan.hu@univ.local",
                                "jack.sun@learnhub.local",
                                "Boyuan Hu",
                                departments.softwareEngineering(),
                                daysAgoAt(7, 10),
                                13),
                        new DemoUser(
                                "siyu.gao@univ.local",
                                "kate.liu@learnhub.local",
                                "Siyu Gao",
                                departments.computerScience(),
                                daysAgoAt(8, 16),
                                19),
                        new DemoUser(
                                "wenqi.xu@univ.local",
                                "leo.xu@learnhub.local",
                                "Wenqi Xu",
                                departments.business(),
                                daysAgoAt(9, 11),
                                10));

        List<User> users = new ArrayList<>();
        for (DemoUser demoUser : demoUsers) {
            users.add(ensureUser(demoUser));
        }
        return users;
    }

    private DemoResources seedResources(Integer adminId) {
        Resource videoOnboarding =
                ensureResource(
                        adminId,
                        BackendConstant.RESOURCE_TYPE_VIDEO,
                        "demo-campus-orientation.mp4",
                        "demo-onboarding-overview.mp4",
                        "mp4",
                        248_000_000L,
                        "videos/demo-campus-orientation.mp4",
                        "videos/demo-onboarding-overview.mp4",
                        1_240);
        Resource videoProgramming =
                ensureResource(
                        adminId,
                        BackendConstant.RESOURCE_TYPE_VIDEO,
                        "demo-python-programming-basics.mp4",
                        "demo-java-service-design.mp4",
                        "mp4",
                        384_000_000L,
                        "videos/demo-python-programming-basics.mp4",
                        "videos/demo-java-service-design.mp4",
                        1_680);
        Resource videoResearch =
                ensureResource(
                        adminId,
                        BackendConstant.RESOURCE_TYPE_VIDEO,
                        "demo-academic-research.mp4",
                        "demo-data-dashboard-basics.mp4",
                        "mp4",
                        296_000_000L,
                        "videos/demo-academic-research.mp4",
                        "videos/demo-data-dashboard-basics.mp4",
                        1_520);
        Resource videoLabSafety =
                ensureResource(
                        adminId,
                        BackendConstant.RESOURCE_TYPE_VIDEO,
                        "demo-lab-safety-ethics.mp4",
                        "demo-security-awareness.mp4",
                        "mp4",
                        210_000_000L,
                        "videos/demo-lab-safety-ethics.mp4",
                        "videos/demo-security-awareness.mp4",
                        1_100);

        ensureResource(
                adminId,
                BackendConstant.RESOURCE_TYPE_IMAGE,
                "demo-course-cover-campus.png",
                "demo-course-cover-product.png",
                "png",
                482_000L,
                "images/demo-course-cover-campus.png",
                "images/demo-course-cover-product.png",
                null);
        ensureResource(
                adminId,
                BackendConstant.RESOURCE_TYPE_IMAGE,
                "demo-course-cover-lab.png",
                "demo-course-cover-engineering.png",
                "png",
                536_000L,
                "images/demo-course-cover-lab.png",
                "images/demo-course-cover-engineering.png",
                null);

        Resource freshmanHandbook =
                ensureResource(
                        adminId,
                        BackendConstant.RESOURCE_TYPE_PDF,
                        "demo-freshman-handbook.pdf",
                        "demo-new-hire-handbook.pdf",
                        "pdf",
                        1_620_000L,
                        "pdf/demo-freshman-handbook.pdf",
                        "pdf/demo-new-hire-handbook.pdf",
                        null);
        Resource programmingSlides =
                ensureResource(
                        adminId,
                        BackendConstant.RESOURCE_TYPE_PPT,
                        "demo-python-lab-slides.pptx",
                        "demo-java-service-slides.pptx",
                        "pptx",
                        3_420_000L,
                        "ppt/demo-python-lab-slides.pptx",
                        "ppt/demo-java-service-slides.pptx",
                        null);
        Resource researchTemplate =
                ensureResource(
                        adminId,
                        BackendConstant.RESOURCE_TYPE_EXCEL,
                        "demo-research-notes-template.xlsx",
                        "demo-learning-metrics-template.xlsx",
                        "xlsx",
                        928_000L,
                        "excel/demo-research-notes-template.xlsx",
                        "excel/demo-learning-metrics-template.xlsx",
                        null);

        return new DemoResources(
                videoOnboarding,
                videoProgramming,
                videoResearch,
                videoLabSafety,
                freshmanHandbook,
                programmingSlides,
                researchTemplate);
    }

    private List<CourseSeed> seedCourses(
            Integer adminId, DemoDepartments departments, DemoResources resources) {
        CourseSeed onboarding =
                ensureCourse(
                        adminId,
                        "Freshman Campus Orientation",
                        "New Hire Onboarding",
                        -1,
                        "Campus services, study rules, and first-week student workflows.",
                        1,
                        new Integer[] {
                            departments.computerScience(),
                            departments.softwareEngineering(),
                            departments.business(),
                            departments.digitalMedia(),
                            departments.generalEducation()
                        },
                        List.of(
                                new LessonSeed(
                                        "Campus Start",
                                        "Start Here",
                                        "Student Portal and Campus Map",
                                        "Welcome to LearnHub",
                                        resources.videoOnboarding().getId(),
                                        620),
                                new LessonSeed(
                                        "Campus Start",
                                        "Start Here",
                                        "Academic Calendar and Study Rules",
                                        "Policies and Compliance",
                                        resources.videoLabSafety().getId(),
                                        760),
                                new LessonSeed(
                                        "Student Services",
                                        "Team Workflows",
                                        "Library and Advising Services",
                                        "Collaboration Basics",
                                        resources.videoOnboarding().getId(),
                                        540)),
                        List.of(
                                new AttachmentSeed(
                                        "Freshman Handbook",
                                        "New Hire Handbook",
                                        BackendConstant.RESOURCE_TYPE_PDF,
                                        resources.freshmanHandbook().getId())));

        CourseSeed programmingBasics =
                ensureCourse(
                        adminId,
                        "Python Programming Fundamentals",
                        "Java Backend Essentials",
                        -2,
                        "Programming basics and lab practice for undergraduate courses.",
                        1,
                        new Integer[] {
                            departments.computerScience(),
                            departments.softwareEngineering(),
                            departments.digitalMedia(),
                            departments.generalEducation()
                        },
                        List.of(
                                new LessonSeed(
                                        "Programming Basics",
                                        "Service Design",
                                        "Python Syntax and Functions",
                                        "Layered Architecture",
                                        resources.videoProgramming().getId(),
                                        840),
                                new LessonSeed(
                                        "Programming Basics",
                                        "Service Design",
                                        "Control Flow and Collections",
                                        "API Error Handling",
                                        resources.videoProgramming().getId(),
                                        780),
                                new LessonSeed(
                                        "Lab Practice",
                                        "Persistence",
                                        "Debugging in the Lab",
                                        "MyBatis Query Patterns",
                                        resources.videoProgramming().getId(),
                                        920),
                                new LessonSeed(
                                        "Lab Practice",
                                        "Persistence",
                                        "Course Project Setup",
                                        "Transaction Boundaries",
                                        resources.videoProgramming().getId(),
                                        860)),
                        List.of(
                                new AttachmentSeed(
                                        "Python Lab Slides",
                                        "Java Service Slides",
                                        BackendConstant.RESOURCE_TYPE_PPT,
                                        resources.programmingSlides().getId())));

        CourseSeed academicWriting =
                ensureCourse(
                        adminId,
                        "Academic Writing and Research",
                        "Data Analysis Basics",
                        -3,
                        "Library research, citations, and short paper structure.",
                        0,
                        new Integer[] {
                            departments.computerScience(),
                            departments.business(),
                            departments.digitalMedia(),
                            departments.generalEducation()
                        },
                        List.of(
                                new LessonSeed(
                                        "Research Skills",
                                        "Metrics",
                                        "Finding Library Sources",
                                        "Reading Learning Metrics",
                                        resources.videoResearch().getId(),
                                        700),
                                new LessonSeed(
                                        "Research Skills",
                                        "Metrics",
                                        "Citation and Plagiarism Basics",
                                        "Weekly Report Template",
                                        resources.videoResearch().getId(),
                                        650),
                                new LessonSeed(
                                        "Writing Workshop",
                                        "Dashboards",
                                        "Structuring a Short Paper",
                                        "Dashboard Review Checklist",
                                        resources.videoResearch().getId(),
                                        740)),
                        List.of(
                                new AttachmentSeed(
                                        "Research Notes Template",
                                        "Learning Metrics Template",
                                        BackendConstant.RESOURCE_TYPE_EXCEL,
                                        resources.researchTemplate().getId())));

        CourseSeed labSafety =
                ensureCourse(
                        adminId,
                        "Laboratory Safety and Ethics",
                        "Security Awareness",
                        -1,
                        "Campus lab safety, account security, and study data ethics.",
                        0,
                        new Integer[] {
                            departments.computerScience(),
                            departments.softwareEngineering(),
                            departments.business(),
                            departments.digitalMedia()
                        },
                        List.of(
                                new LessonSeed(
                                        "Lab Safety",
                                        "Account Safety",
                                        "Device and Account Safety",
                                        "Password and MFA Basics",
                                        resources.videoLabSafety().getId(),
                                        520),
                                new LessonSeed(
                                        "Research Ethics",
                                        "Data Safety",
                                        "Handling Study Data",
                                        "Handling Customer Data",
                                        resources.videoLabSafety().getId(),
                                        580)),
                        List.of());

        return List.of(onboarding, programmingBasics, academicWriting, labSafety);
    }

    private void seedLearningRecords(List<User> users, List<CourseSeed> courses) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            CourseSeed primaryCourse = courses.get(i % courses.size());
            CourseSeed secondaryCourse = courses.get((i + 1) % courses.size());

            seedCourseProgress(
                    user, primaryCourse, Math.min(primaryCourse.hours().size(), 1 + i % 4));
            if (i % 2 == 0) {
                seedCourseProgress(
                        user, secondaryCourse, Math.min(secondaryCourse.hours().size(), 1 + i % 2));
            }
        }
    }

    private void seedCourseProgress(User user, CourseSeed courseSeed, int finishedCount) {
        List<CourseHour> hours = courseSeed.hours();
        for (int i = 0; i < hours.size(); i++) {
            CourseHour hour = hours.get(i);
            Integer watchedDuration =
                    i < finishedCount ? hour.getDuration() : hour.getDuration() / 2;
            boolean finished =
                    userCourseHourRecordService.storeOrUpdate(
                            user.getId(),
                            courseSeed.course().getId(),
                            hour.getId(),
                            watchedDuration,
                            hour.getDuration());

            if (!finished && i >= finishedCount) {
                break;
            }
        }

        userCourseRecordService.storeOrUpdate(
                user.getId(), courseSeed.course().getId(), hours.size(), finishedCount);
    }

    private void seedLearningStats(List<User> users) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        long[] todayDurations = {
            7_200_000L,
            6_480_000L,
            5_940_000L,
            5_400_000L,
            4_980_000L,
            4_560_000L,
            4_020_000L,
            3_600_000L,
            3_180_000L,
            2_700_000L
        };

        for (int i = 0; i < users.size(); i++) {
            LocalDate date = i < todayDurations.length ? today : yesterday;
            long duration =
                    i < todayDurations.length ? todayDurations[i] : 1_800_000L + i * 300_000L;
            ensureLearnDurationStats(users.get(i).getId(), date, duration);
        }

        if (users.size() >= 4) {
            ensureLearnDurationStats(users.get(0).getId(), yesterday, 3_300_000L);
            ensureLearnDurationStats(users.get(1).getId(), yesterday, 2_700_000L);
            ensureLearnDurationStats(users.get(2).getId(), yesterday, 2_100_000L);
            ensureLearnDurationStats(users.get(3).getId(), yesterday, 1_800_000L);
        }
    }

    private Integer ensureDepartment(String name, String legacyName, Integer parentId, Integer sort)
            throws NotFoundException {
        Department department = departmentService.findByName(name, parentId);
        if (department == null && legacyName != null) {
            department = departmentService.findByName(legacyName, parentId);
        }
        if (department != null) {
            departmentService.update(department, name, parentId, sort);
            return department.getId();
        }
        return departmentService.create(name, parentId, sort);
    }

    private User ensureUser(DemoUser demoUser) {
        User user = userService.find(demoUser.email());
        if (user == null && demoUser.legacyEmail() != null) {
            user = userService.find(demoUser.legacyEmail());
        }
        if (user == null) {
            user =
                    userService.createWithDepIds(
                            demoUser.email(),
                            demoUser.name(),
                            0,
                            DEMO_PASSWORD,
                            "",
                            new Integer[] {demoUser.departmentId()});
        } else {
            userService.updateDepId(user.getId(), new Integer[] {demoUser.departmentId()});
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(demoUser.email());
        updateUser.setName(demoUser.name());
        updateUser.setAvatar(0);
        updateUser.setCredit1(demoUser.credit());
        updateUser.setIsActive(1);
        updateUser.setIsLock(0);
        updateUser.setIsSetPassword(1);
        updateUser.setCreatedAt(demoUser.createdAt());
        updateUser.setUpdatedAt(new Date());
        userService.updateById(updateUser);

        return userService.find(user.getId());
    }

    private Resource ensureResource(
            Integer adminId,
            String type,
            String name,
            String legacyName,
            String extension,
            Long size,
            String path,
            String legacyPath,
            Integer duration) {
        Resource resource =
                resourceService.getOne(
                        resourceService
                                .query()
                                .getWrapper()
                                .eq("name", name)
                                .eq("path", path)
                                .last("limit 1"));
        if (resource == null && legacyName != null && legacyPath != null) {
            resource =
                    resourceService.getOne(
                            resourceService
                                    .query()
                                    .getWrapper()
                                    .eq("name", legacyName)
                                    .eq("path", legacyPath)
                                    .last("limit 1"));
        }
        if (resource == null) {
            resource =
                    resourceService.create(
                            adminId,
                            type,
                            name,
                            extension,
                            size,
                            BackendConstant.STORAGE_DRIVER_MINIO,
                            path,
                            0,
                            0);
        } else {
            Resource updateResource = new Resource();
            updateResource.setId(resource.getId());
            updateResource.setAdminId(adminId);
            updateResource.setType(type);
            updateResource.setName(name);
            updateResource.setExtension(extension);
            updateResource.setSize(size);
            updateResource.setDisk(BackendConstant.STORAGE_DRIVER_MINIO);
            updateResource.setPath(path);
            updateResource.setParentId(0);
            updateResource.setIsHidden(0);
            resourceService.updateById(updateResource);
        }

        if (duration != null) {
            ensureResourceExtra(resource.getId(), duration);
        }
        return resource;
    }

    private void ensureResourceExtra(Integer resourceId, Integer duration) {
        ResourceExtra extra =
                resourceExtraService.getOne(
                        resourceExtraService
                                .query()
                                .getWrapper()
                                .eq("rid", resourceId)
                                .last("limit 1"));
        if (extra == null) {
            resourceExtraService.create(resourceId, duration, 0);
            return;
        }

        ResourceExtra updateExtra = new ResourceExtra();
        updateExtra.setId(extra.getId());
        updateExtra.setDuration(duration);
        updateExtra.setPoster(0);
        resourceExtraService.updateById(updateExtra);
    }

    private CourseSeed ensureCourse(
            Integer adminId,
            String title,
            String legacyTitle,
            Integer thumb,
            String shortDesc,
            Integer isRequired,
            Integer[] depIds,
            List<LessonSeed> lessons,
            List<AttachmentSeed> attachments) {
        Course course =
                courseService.getOne(
                        courseService.query().getWrapper().eq("title", title).last("limit 1"));
        if (course == null && legacyTitle != null) {
            course =
                    courseService.getOne(
                            courseService
                                    .query()
                                    .getWrapper()
                                    .eq("title", legacyTitle)
                                    .last("limit 1"));
        }
        if (course == null) {
            course =
                    courseService.createWithDepIds(
                            title, thumb, shortDesc, isRequired, 1, depIds, adminId);
        } else {
            Course updateCourse = new Course();
            updateCourse.setId(course.getId());
            updateCourse.setTitle(title);
            updateCourse.setThumb(thumb);
            updateCourse.setShortDesc(shortDesc);
            updateCourse.setIsRequired(isRequired);
            updateCourse.setIsShow(1);
            updateCourse.setAdminId(adminId);
            updateCourse.setSortAt(new Date());
            updateCourse.setUpdatedAt(new Date());
            courseService.updateById(updateCourse);
            courseService.resetRelateDepartments(course, depIds);
        }

        List<CourseHour> hours = ensureLessons(course.getId(), lessons);
        ensureAttachments(course.getId(), attachments);
        courseService.updateClassHour(course.getId(), hours.size());

        Course refreshedCourse = courseService.getById(course.getId());
        return new CourseSeed(refreshedCourse, hours);
    }

    private List<CourseHour> ensureLessons(Integer courseId, List<LessonSeed> lessons) {
        List<CourseHour> hours = new ArrayList<>();
        List<String> chapterNames = lessons.stream().map(LessonSeed::chapter).distinct().toList();

        for (int chapterIndex = 0; chapterIndex < chapterNames.size(); chapterIndex++) {
            String chapterName = chapterNames.get(chapterIndex);
            List<LessonSeed> chapterLessons =
                    lessons.stream()
                            .filter(lesson -> lesson.chapter().equals(chapterName))
                            .toList();
            CourseChapter chapter =
                    ensureChapter(
                            courseId,
                            chapterName,
                            chapterLessons.get(0).legacyChapter(),
                            chapterIndex);

            for (int lessonIndex = 0; lessonIndex < chapterLessons.size(); lessonIndex++) {
                LessonSeed lesson = chapterLessons.get(lessonIndex);
                hours.add(
                        ensureHour(
                                courseId,
                                chapter.getId(),
                                lessonIndex,
                                lesson.title(),
                                lesson.legacyTitle(),
                                lesson.resourceId(),
                                lesson.duration()));
            }
        }

        return hours;
    }

    private CourseChapter ensureChapter(
            Integer courseId, String name, String legacyName, Integer sort) {
        CourseChapter chapter =
                chapterService.getOne(
                        chapterService
                                .query()
                                .getWrapper()
                                .eq("course_id", courseId)
                                .eq("name", name)
                                .last("limit 1"));
        if (chapter == null && legacyName != null) {
            chapter =
                    chapterService.getOne(
                            chapterService
                                    .query()
                                    .getWrapper()
                                    .eq("course_id", courseId)
                                    .eq("name", legacyName)
                                    .last("limit 1"));
        }
        if (chapter == null) {
            chapter = new CourseChapter();
            chapter.setCourseId(courseId);
            chapter.setName(name);
            chapter.setSort(sort);
            chapter.setCreatedAt(new Date());
            chapter.setUpdatedAt(new Date());
            chapterService.save(chapter);
            return chapter;
        }

        CourseChapter updateChapter = new CourseChapter();
        updateChapter.setId(chapter.getId());
        updateChapter.setName(name);
        updateChapter.setSort(sort);
        updateChapter.setUpdatedAt(new Date());
        chapterService.updateById(updateChapter);
        return chapter;
    }

    private CourseHour ensureHour(
            Integer courseId,
            Integer chapterId,
            Integer sort,
            String title,
            String legacyTitle,
            Integer resourceId,
            Integer duration) {
        CourseHour hour =
                hourService.getOne(
                        hourService
                                .query()
                                .getWrapper()
                                .eq("course_id", courseId)
                                .eq("title", title)
                                .last("limit 1"));
        if (hour == null && legacyTitle != null) {
            hour =
                    hourService.getOne(
                            hourService
                                    .query()
                                    .getWrapper()
                                    .eq("course_id", courseId)
                                    .eq("title", legacyTitle)
                                    .last("limit 1"));
        }
        if (hour == null) {
            return hourService.create(
                    courseId,
                    chapterId,
                    sort,
                    title,
                    BackendConstant.RESOURCE_TYPE_VIDEO,
                    resourceId,
                    duration);
        }

        CourseHour updateHour = new CourseHour();
        updateHour.setId(hour.getId());
        updateHour.setChapterId(chapterId);
        updateHour.setSort(sort);
        updateHour.setTitle(title);
        updateHour.setType(BackendConstant.RESOURCE_TYPE_VIDEO);
        updateHour.setRid(resourceId);
        updateHour.setDuration(duration);
        updateHour.setDeleted(0);
        hourService.updateById(updateHour);

        return hourService.getById(hour.getId());
    }

    private void ensureAttachments(Integer courseId, List<AttachmentSeed> attachments) {
        for (int i = 0; i < attachments.size(); i++) {
            AttachmentSeed attachment = attachments.get(i);
            ensureAttachment(courseId, i, attachment);
        }
    }

    private void ensureAttachment(Integer courseId, Integer sort, AttachmentSeed attachment) {
        CourseAttachment courseAttachment =
                attachmentService.getOne(
                        attachmentService
                                .query()
                                .getWrapper()
                                .eq("course_id", courseId)
                                .eq("title", attachment.title())
                                .last("limit 1"));
        if (courseAttachment == null && attachment.legacyTitle() != null) {
            courseAttachment =
                    attachmentService.getOne(
                            attachmentService
                                    .query()
                                    .getWrapper()
                                    .eq("course_id", courseId)
                                    .eq("title", attachment.legacyTitle())
                                    .last("limit 1"));
        }
        if (courseAttachment == null) {
            attachmentService.create(
                    courseId, sort, attachment.title(), attachment.type(), attachment.resourceId());
            return;
        }

        CourseAttachment updateAttachment = new CourseAttachment();
        updateAttachment.setId(courseAttachment.getId());
        updateAttachment.setSort(sort);
        updateAttachment.setTitle(attachment.title());
        updateAttachment.setType(attachment.type());
        updateAttachment.setRid(attachment.resourceId());
        attachmentService.updateById(updateAttachment);
    }

    private void ensureLearnDurationStats(Integer userId, LocalDate date, Long duration) {
        UserLearnDurationStats stats =
                userLearnDurationStatsService.getOne(
                        userLearnDurationStatsService
                                .query()
                                .getWrapper()
                                .eq("user_id", userId)
                                .eq("created_date", java.sql.Date.valueOf(date))
                                .last("limit 1"));
        if (stats == null) {
            stats = new UserLearnDurationStats();
            stats.setUserId(userId);
            stats.setDuration(duration);
            stats.setCreatedDate(java.sql.Date.valueOf(date));
            userLearnDurationStatsService.save(stats);
            return;
        }

        UserLearnDurationStats updateStats = new UserLearnDurationStats();
        updateStats.setId(stats.getId());
        updateStats.setDuration(duration);
        userLearnDurationStatsService.updateById(updateStats);
    }

    private Date recentMinutesAgo(int minutes) {
        return new Date(System.currentTimeMillis() - minutes * 60_000L);
    }

    private Date yesterdayAt(int hour) {
        return daysAgoAt(1, hour);
    }

    private Date daysAgoAt(int days, int hour) {
        return Date.from(
                LocalDate.now()
                        .minusDays(days)
                        .atTime(hour, 0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    private record DemoDepartments(
            Integer computerScience,
            Integer softwareEngineering,
            Integer business,
            Integer digitalMedia,
            Integer generalEducation) {}

    private record DemoResources(
            Resource videoOnboarding,
            Resource videoProgramming,
            Resource videoResearch,
            Resource videoLabSafety,
            Resource freshmanHandbook,
            Resource programmingSlides,
            Resource researchTemplate) {}

    private record DemoUser(
            String email,
            String legacyEmail,
            String name,
            Integer departmentId,
            Date createdAt,
            Integer credit) {}

    private record LessonSeed(
            String chapter,
            String legacyChapter,
            String title,
            String legacyTitle,
            Integer resourceId,
            Integer duration) {}

    private record AttachmentSeed(
            String title, String legacyTitle, String type, Integer resourceId) {}

    private record CourseSeed(Course course, List<CourseHour> hours) {}
}
