import React, { useState, useEffect } from "react";
import axios from "axios";
import { useSelector } from "react-redux";
import "./InquiryWriteForm.css";

import { useNavigate } from "react-router-dom";

function InquiryWriteForm() {
  const [word, setWord] = useState(null);
  const loginUser = useSelector((state) => state.user);

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [appendImage, setAppendImage] = useState("");
  const [appendImageSrc, setAppendImageSrc] = useState("");
  const [appendImageStyle, setAppendImageStyle] = useState({ display: "none" });

  const navigate = useNavigate();

  function onSubmit() {
    axios
      .post("/api/inquiries", {
        userid: loginUser.userid,
        email: loginUser.email,
        title,
        content,
        appendImage,
      })
      .then(() => {
        navigate("/inquiryList");
      })
      .catch((err) => {
        console.error(err);
      });
  }

  async function onFileUpload(e) {
    const formData = new FormData();
    formData.append("appendImage", e.target.files[0]);

    const result = await axios.post("/api/inquiries/fileupload", formData);
    setAppendImage(result.data.image);

    setAppendImageSrc(`http://localhost:8070/images/${result.data.image}`);
    setAppendImageStyle({ width: "200px", height: "200px", display: "block" });
    // setImgStyle({ width: "800px", display: "block" });
  }

  return (
    <div>
      <br></br>
      <br></br>
      <br></br>
      <br></br>
      <br></br>
      <div className="iqBody">
        <div id="iqWriteCenter">
          <div id="iqwf1">문의 작성</div>
          <br></br>
          <div id="iqwrite">
            <div className="iqwfField">
              <input
                type="text"
                placeholder="제목"
                style={{ fontSize: "30px" }}
                onChange={(e) => {
                  setTitle(e.currentTarget.value);
                }}
              />
            </div>
            <br></br>
            <br></br>
            <div className="iqwfField">
              <textarea
                rows="20"
                placeholder="문의내용을 작성해주세요."
                style={{ fontSize: "30px" }}
                onChange={(e) => {
                  setContent(e.currentTarget.value);
                }}
              ></textarea>
            </div>
            <br></br>
            <br></br>
            <div className="iqwfField">
              <label>사진 첨부</label>
              <br />
              <input
                type="file"
                style={{ fontSize: "20px" }}
                onChange={(e) => {
                  onFileUpload(e);
                }}
              />
              <br />
              <div>
                <img
                  src={appendImageSrc}
                  style={appendImageStyle}
                  alt="appendImage"
                />
              </div>
            </div>
          </div>
          <br></br>
          <div className="inquiryinquiry">
            <div
              className="iqwf2"
              onClick={() => {
                onSubmit();
              }}
            >
              작성 완료
            </div>
            <div
              className="iqwf2"
              onClick={() => {
                navigate("/inquiryList");
              }}
            >
              돌아가기
            </div>
          </div>
          <br></br>
        </div>
      </div>
    </div>
  );
}

export default InquiryWriteForm;
