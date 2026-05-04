import React, { useEffect, useState } from "react";
import api from "../services/api";
import Layout from "../components/Layout";
import { useLocation } from "react-router-dom";

function Profile() {

  const [user,setUser] = useState({});
  const [fullName,setFullName] = useState("");

  const [oldPassword,setOldPassword] = useState("");
  const [newPassword,setNewPassword] = useState("");

  const location = useLocation();

  const tab =
    new URLSearchParams(location.search)
    .get("tab");

  useEffect(() => {
    loadProfile();
  }, []);

  const loadProfile = async() => {
    try{
      const res = await api.get("/auth/me");
      setUser(res.data);
      setFullName(res.data.fullName || "");
    }catch(error){
      alert("Failed to load profile");
    }
  };

  const updateProfile = async() => {
    try{
      const res = await api.put(
        "/auth/update-profile",
        { fullName }
      );

      alert(res.data);
      loadProfile();

    }catch(error){
      alert("Update Failed");
    }
  };

  const changePassword = async() => {
    try{
      const res = await api.put(
        "/auth/change-password",
        {
          oldPassword,
          newPassword
        }
      );

      alert(res.data);

      setOldPassword("");
      setNewPassword("");

    }catch(error){
      alert("Password Change Failed");
    }
  };

  const firstLetter =
    user.fullName
    ? user.fullName.charAt(0).toUpperCase()
    : "U";

  return (
    <Layout>

      <div className="profile-page">

        <h1 className="profile-title">
          My Profile
        </h1>

        <div className="profile-grid">

          {/* LEFT CARD */}
          <div className="profile-card left-card">

            <div className="avatar">
              {firstLetter}
            </div>

            <h2>{user.fullName}</h2>

            <span className="role-badge">
              {user.role}
            </span>

            <p>{user.email}</p>

            <p>
              Organization: {user.organizationId}
            </p>

            <p>
              Status: {user.status}
            </p>

          </div>

          {/* RIGHT CARD */}
          <div className="profile-card right-card">

            {(tab === "view" || !tab) && (
              <>
                <h3>Update Profile</h3>

                <input
                  value={fullName}
                  placeholder="Full Name"
                  onChange={(e)=>
                    setFullName(e.target.value)
                  }
                />

                <button
                  onClick={updateProfile}
                  className="save-btn"
                >
                  Save Changes
                </button>
              </>
            )}

            {tab === "password" && (
              <>
                <h3>Change Password</h3>

                <input
                  type="password"
                  value={oldPassword}
                  placeholder="Old Password"
                  onChange={(e)=>
                    setOldPassword(e.target.value)
                  }
                />

                <input
                  type="password"
                  value={newPassword}
                  placeholder="New Password"
                  onChange={(e)=>
                    setNewPassword(e.target.value)
                  }
                />

                <button
                  onClick={changePassword}
                  className="pass-btn"
                >
                  Change Password
                </button>
              </>
            )}

          </div>

        </div>

      </div>

    </Layout>
  );
}

export default Profile;