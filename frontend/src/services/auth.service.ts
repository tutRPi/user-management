import axios from "axios";

const API_URL = "http://localhost:8080/api/v1/user/"; // TODO externalize

class AuthService {
    login(username: string, password: string) {
        return axios
            .post(API_URL + "auth", {
                username,
                password
            })
            .then(response => {
                if (response.data.jwt) {
                    localStorage.setItem("user", JSON.stringify(response.data));
                }

                return response.data;
            });
    }

    logout() {
        localStorage.removeItem("user");
    }

    register(username: string, email: string, password: string, passwordConfirmation: string, firstName: string, lastName: string, t2FAEnabled: boolean) {
        return axios.post(API_URL + "signup", {
            username,
            email,
            password,
            passwordConfirmation,
            firstName,
            lastName,
            t2FAEnabled
        });
    }

    getCurrentUser() {
        const userStr = localStorage.getItem("user");
        if (userStr) return JSON.parse(userStr);

        return null;
    }
}

export default new AuthService();
