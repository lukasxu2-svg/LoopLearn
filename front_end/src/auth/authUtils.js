export function isTokenExpired() {
    const expiresIn = localStorage.getItem("expiresIn");
    return new Date(expiresIn).getTime() < Date.now();
}