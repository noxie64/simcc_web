export function useLoginStatus() {

    const loggedIn = () => {
        const loggedInStorage = localStorage.getItem('loggedIn');
        return loggedInStorage === 'true';
    }

    const setLoggedIn = () => {
        localStorage.setItem('loggedIn', 'true');
    }

    return {
        loggedIn,
        setLoggedIn
    }
}
