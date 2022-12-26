package com.wwp.entity;

public class LoginUser {


        /**
         * 登录人账号
         */
        private String username;

        /**
         * 登录人密码
         */
        private String password;

         private String openId;

         private String type;

        public String getUsername(){
            return this.username;
        }
        public String getPassword(){
            return this.password;
        }
        public void setUsername(String username){
             this.username=username;
        }
        public void setPassword(String password){
             this.password=password;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
}
