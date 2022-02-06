export default function authHeader(): { Authorization?: string } {
    const userStr = localStorage.getItem("user");
    let user = null;
    if (userStr)
        user = JSON.parse(userStr);

    console.log(user)
    if (user && user.jwt) {
        return {Authorization: 'Bearer ' + user.jwt};
    } else {
        return {};
    }
}
