import axios from "axios";
import authHeader from "./auth-header";

const API_URL = "http://localhost:8080/api/v1/user/"; // TODO externalize

interface AuthenticationResponse {
    jwt: string,
    mustVerify2FACode: boolean
}

class AuthService {
    login(username: string, password: string): Promise<AuthenticationResponse> {
        return axios
            .post(API_URL + "auth", {
                username,
                password
            })
            .then((response: { data: AuthenticationResponse }) => {
                if (response.data.jwt) {
                    localStorage.setItem("user", JSON.stringify({
                        username,
                        ...response.data
                    }));
                }

                return response.data;
            });
    }

    twoFA(t2FACode: string): Promise<AuthenticationResponse> {
        return axios
            .post(API_URL + "2fa", {
                t2FACode
            }, {headers: authHeader()})
            .then((response: { data: AuthenticationResponse }) => {
                if (response.data.jwt) {
                    // TODO add more info (roles, etc)
                    localStorage.setItem("user", JSON.stringify(response.data));
                }

                return response.data;
            });
    }

    resetTwoFA(code: string): Promise<boolean> {
        return axios
            .delete(API_URL + "reset_2fa", {
                headers: authHeader(),
                data: {
                    code
                }
            })
            .then(() => {
                return true
            });
    }

    logout() {
        localStorage.removeItem("user");
    }

    register(email: string, password: string, passwordConfirmation: string, firstName: string, lastName: string, t2FAEnabled: boolean) {
        return axios.post(API_URL + "signup", {
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
