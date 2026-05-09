import { UploadVideoButton } from "../../compenents/upload-video-button";

const TestPage = () => {
  return (
    <div>
      <UploadVideoButton
        onUpdate={() => {
          console.log(123);
        }}
      ></UploadVideoButton>
    </div>
  );
};

export default TestPage;
