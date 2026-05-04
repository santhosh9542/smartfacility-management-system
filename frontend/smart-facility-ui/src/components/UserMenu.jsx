import React, { useEffect, useRef, useState } from "react";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

function UserMenu() {

  const [user,setUser] = useState({});
  const [open,setOpen] = useState(false);

  const fileRef = useRef();
  const navigate = useNavigate();

  useEffect(() => {
    loadUser();
  }, []);

  const loadUser = async() => {
    try{
      const res = await api.get("/auth/me");
      setUser(res.data);
    }catch(error){}
  };

  const logout = () => {
    localStorage.removeItem("token");
    navigate("/");
  };

  const uploadPhoto = async(e) => {

    const file = e.target.files[0];

    if(!file) return;

    const formData = new FormData();
    formData.append("file", file);

    try{
      await api.post(
        "/auth/upload-photo",
        formData,
        {
          headers:{
            "Content-Type":
            "multipart/form-data"
          }
        }
      );

      loadUser();
      alert("Photo Updated");

    }catch(error){
      alert("Upload Failed");
    }
  };

  const firstLetter =
    user.fullName
    ? user.fullName.charAt(0).toUpperCase()
    : "U";

  const photoUrl =
    user.profilePhoto
    ? "http://localhost:8080/uploads/profile/" +
      user.profilePhoto
    : null;

  return (
    <div className="user-menu">

      <div
        className="avatar-small"
        onClick={() => setOpen(!open)}
      >

        {photoUrl ? (
          <img
            src={photoUrl}
            alt=""
          />
        ) : (
          firstLetter
        )}

      </div>

      {open && (

        <div className="dropdown-menu">

<div
  onClick={() => navigate("/profile?tab=view")}
>
  👤 View Profile
</div>

<div
  onClick={() => fileRef.current.click()}
>
  🖼 Upload Photo
</div>

<div
  onClick={() => navigate("/profile?tab=password")}
>
  🔒 Change Password
</div>

<div onClick={logout}>
  🚪 Logout
</div>

        </div>
      )}

      <input
        type="file"
        hidden
        ref={fileRef}
        onChange={uploadPhoto}
      />

    </div>
  );
}

export default UserMenu;