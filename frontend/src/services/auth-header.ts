export default function authHeader() {
    const userStr = localStorage.getItem("user");
    let user = null;
    if (userStr)
        user = JSON.parse(userStr);

    if (user && user.jwt) {
        return {Authorization: 'Bearer ' + user.jwt};
    } else {
        return {};
    }
}
