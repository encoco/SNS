import React, { useState } from 'react';
import Modal from 'react-modal';

function Setting({ isOpen, onRequestClose, onClose }) {
  // State to manage the validity of the new password
  const [passwordValidity, setPasswordValidity] = useState({
    isValid: false,
    password: '',
  });

  // Function to validate the password
  const validatePassword = (password) => {
    const regex = /^(?=.*[a-z])(?=.*[0-9])(?=.*[\W_]).{8,20}$/;
    setPasswordValidity({
      isValid: regex.test(password),
      password,
    });
  };

  return (
    <Modal
      isOpen={isOpen}
      className="fixed inset-0 bg-opacity-50 flex justify-center items-center z-10 "
      overlayClassName="fixed inset-0 bg-black bg-opacity-5"
      onRequestClose={() => {
        onRequestClose();
        onClose(); // onClose 함수 호출
      }}
      style={{ zIndex: 12 }}
    >
      <div className="w-[75vw] max-w-sm mx-auto relative bg-white p-5 rounded-lg">
        <h2 className="text-xl font-semibold mb-4">환경 설정</h2>
        <form>
          <h3 className="text-lg font-semibold mb-2">비밀번호 변경</h3>
          <label htmlFor="current-password" className="block mb-2">
            현재 비밀번호
            <input
              type="password"
              id="current-password"
              name="current-password"
              className="border border-gray-300 rounded-md p-2 mt-1 ml-4"
            />
          </label>
          <label htmlFor="new-password" className="block mb-2">
            새 비밀번호
            <input
              type="password"
              id="new-password"
              name="new-password"
              className="border border-gray-300 rounded-md p-2 mt-1 ml-8"
              style={{
                borderColor: passwordValidity.isValid ? 'green' : 'red'
              }}
              onChange={(e) => validatePassword(e.target.value)}
            /><br/>
            <i className="text-sm mt-3">
              *비밀번호는 8자 이상 20자 미만이며, 소문자, 숫자, 특수기호를 각각 하나 이상 포함해야 합니다.
            </i>
          </label>
          <label htmlFor="confirm-password" className="block mb-2">
            비밀번호 확인
            <input
              type="password"
              id="confirm-password"
              name="confirm-password"
              className="border border-gray-300 rounded-md p-2 mt-1 ml-4"
            />
          </label>
          <button type="submit" className="bg-black text-white py-2 px-4 rounded-md mt-4">비밀번호 변경</button>
        </form>
        <div className="mt-6">
          <h3 className="text-lg font-semibold mb-2">회원 탈퇴</h3>
          <p>회원 탈퇴를 원하시면 아래 버튼을 클릭하세요.</p>
          <button className="bg-black text-white py-2 px-4 rounded-md mt-4">회원 탈퇴</button>
        </div>
        <button onClick={onRequestClose} className="absolute top-2 right-2 text-2xl font-bold text-gray-800">&times;</button>
      </div>
    </Modal>
  );
}

export default Setting;
