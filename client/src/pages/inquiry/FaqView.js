import React, { useState, useEffect } from "react";

import "./FaqView.css";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";

function FaqView() {
  const [word, setWord] = useState(null);
  const [qnaView, setQnaView] = useState({});

  const navigate = useNavigate();
  const { id } = useParams();

  useEffect(() => {
    axios
      .get(`/api/inquiries/${id}`)
      .then((result) => {
        setQnaView(result.data);
      })

      .catch((err) => {
        console.error(err);
      });
  }, []);
  return (
    <div>
      <Header setWord={setWord} />
      <div id="qvbody">
        <br></br>
        <br></br>
        <div id="qvcontainer">
          <div id="head">
            <div id="title">{qnaView.title}</div>
            <br></br>
            <div id="date">{qnaView.date}</div>
          </div>
          <div id="content">{qnaView.content}</div>
        </div>
        <br></br>
        <div
          id="back"
          onClick={() => {
            navigate(`/faq`);
          }}
        >
          목록으로
        </div>
        <br></br>
      </div>
      <Footer />
    </div>
  );
}

export default FaqView;
