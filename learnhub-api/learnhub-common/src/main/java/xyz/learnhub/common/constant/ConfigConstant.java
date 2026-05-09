package xyz.learnhub.common.constant;

import java.util.List;

/**
 *
 * @create 2023/4/11 10:12
 */
public class ConfigConstant {

    public static final String SYSTEM_LOGO = "system.logo";
    public static final String SYSTEM_API_URL = "system.api_url";

    public static final String MEMBER_DEFAULT_AVATAR = "member.default_avatar";

    public static final String S3_SERVICE = "s3.service";
    public static final String S3_ACCESS_KEY = "s3.access_key";
    public static final String S3_SECRET_KEY = "s3.secret_key";
    public static final String S3_BUCKET = "s3.bucket";
    public static final String S3_REGION = "s3.region";
    public static final String S3_ENDPOINT = "s3.endpoint";
    public static final String S3_DOMAIN = "s3.domain";

    public static final List<String> REMOVED_CONFIG_KEYS =
            List.of(
                    "system.name",
                    "system.pc_url",
                    "system.pc_index_footer_msg",
                    "player.is_enabled_bullet_secret",
                    "player.bullet_secret_text",
                    "player.bullet_secret_color",
                    "player.bullet_secret_opacity");
}
