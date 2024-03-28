import axios from "axios";



const API_URL = "/api/auth/";
const API_LOGOUT = "/logout";



class AuthService {
  login(username, password) {
    return axios
      .post(API_URL + "signin", {
        username,
        password
      })
      .then(response => {
        if (response.data.accessToken) {
          localStorage.setItem("user", JSON.stringify(response.data));
        }

        return response.data;
      });
  }

  logout() {
    
    const user = JSON.parse(localStorage.getItem("user"));
    const accessToken = user?.accessToken;

    const config = {
      headers: {
          Authorization: `Bearer ${accessToken}`
      }
  };
    
    return axios
    .post(API_LOGOUT, {}, config)
    .then(response => {
      if (response.data) {
        localStorage.removeItem("user");
        
        
      }
      return response.data;
    })
    

  }

  register(username, email, password) {
    return axios.post(API_URL + "signup", {
      username,
      email,
      password
    });
  }

  getCurrentUser() {
    const user = JSON.parse(localStorage.getItem('user'));
    return user;
  }
  

  isAuthenticated() {
    const user = this.getCurrentUser();
    return user && user.accessToken ? true : false;

  }
}

export default new AuthService();