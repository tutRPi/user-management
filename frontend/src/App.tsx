import React from 'react';
import './App.css';
import "bootstrap/dist/css/bootstrap.min.css";
import MainLayout from "./MainLayout";
import {useRoutes, Navigate} from "react-router-dom";
import Login from "./components/pages/login.component";
import Register from "./components/pages/register.component";
import Home from "./components/pages/home.component";
import Profile from "./components/pages/profile.component";

function App() {

    const routes = [{
        path: '/',
        element: <MainLayout/>,
        children: [
            {path: '*', element: <Navigate to='/404'/>},
            {path: '/', element: <Home/>},
            {path: '/profile', element: <Profile/>},
            {path: '/login', element: <Login/>},
            {path: '/register', element: <Register/>}
        ],
    }];

    const routing = useRoutes(routes);

    return <>{routing}</>;
}

export default App;
