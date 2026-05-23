import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { BrowserRouter, Navigate, Route, Routes } from 'react-router'
import NavLayout from './components/NavLayout'
import Dummy from './pages/Dummy'
import { Initialization } from "./pages/Initialization.tsx";
import { TwoFA } from "./pages/TwoFA.tsx";
import { Login } from "./pages/Login.tsx";

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <BrowserRouter>
            <Routes>
                <Route element={<NavLayout />} path='/'>
                    <Route index element={<Navigate to="/infected" replace />} />
                    <Route path='/infected' element={<Dummy />} />
                    <Route path='/settings' element={<Dummy />} />
                    <Route path='/trojans' element={<Dummy />} />
                </Route>
                <Route path='/init' element={<Initialization />} />
                <Route path='/twofa' element={<TwoFA />} />
                <Route path='/login' element={<Login />} />
            </Routes>
        </BrowserRouter>
    </StrictMode>,
)
