import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import SubMenu from "../SubMenu";
import { axios } from "utils";
import { usePageResponse } from "hooks";
import { PaginationNav } from "components/util";

function InquiryList() {
  const navigate = useNavigate();

  const { content, pagination, setPageResponse } = usePageResponse();
  const [word, setWord] = useState("");

  const onSelectPage = useCallback(
    (page) =>
      axios
        .get(`/api/inquiries`, {
          params: { page },
        })
        .then((result) => setPageResponse(result.data))
        .catch(console.error),
    [setPageResponse],
  );

  useEffect(() => {
    if (content.length === 0) {
      onSelectPage(0);
    }
  }, [content, onSelectPage]);

  function userinquiryiew(id) {
    navigate(`/iView/${id}`);
  }

  function onSearch() {
    navigate(`/searchIList/${word}`);
  }

  return (
    <div className="adminContainer">
      <SubMenu />
      <div className="adminbtns" style={{ display: "flex", margin: "5px" }}>
        <input
          type="text"
          className="adminSearch"
          onChange={(e) => {
            setWord(e.currentTarget.value);
          }}
        />
        <button
          onClick={() => {
            onSearch();
          }}
          style={{ fontSize: "25px" }}
        >
          회원ID 검색
        </button>
      </div>
      <div className="productTable">
        <div className="adminrow">
          <div className="admincol">번호</div>
          <div className="admincol">문의제목</div>
          <div className="admincol">회원ID</div>
          <div className="admincol">등록날짜</div>
          <div className="admincol">답변여부</div>
        </div>
        {content.map((inquirylist, idx) => {
          return (
            <div
              className="adminrow"
              key={idx}
              to={`/inquiryView/${inquirylist.id}`}
            >
              <div className="admincol">{inquirylist.id}</div>
              <div
                className="admincol"
                onClick={() => {
                  userinquiryiew(inquirylist.id);
                }}
              >
                {inquirylist.title}
              </div>
              <div className="admincol">{inquirylist.username}</div>
              <div className="admincol">
                {inquirylist.date.substring(0, 10)}
              </div>
              <div className="admincol">
                {inquirylist.answer ? (
                  <div style={{ color: "green", fontWeight: "bold" }}>
                    답변완료
                  </div>
                ) : (
                  <div style={{ color: "grey" }}>답변처리중</div>
                )}
              </div>
            </div>
          );
        })}
        <br></br>
        <PaginationNav {...{ pagination, onSelectPage }} />
      </div>
    </div>
  );
}

export default InquiryList;
