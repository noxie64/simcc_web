import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { BrowserRouter, Navigate, Route, Routes } from 'react-router'
import NavLayout from './components/NavLayout'
import { Initialization } from "./pages/Initialization.tsx";
import { TwoFA } from "./pages/TwoFA.tsx";
import { Login } from "./pages/Login.tsx";
import Trojans from './pages/Trojans.tsx'
import { InfectedPage } from "./pages/Infected.tsx"
import { EnsureLoggedIn } from './components/EnsureLoggedIn.tsx'
import { InfectedControlRoom } from './pages/InfectedControlRoom.tsx'

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <BrowserRouter>
            <Routes>
                <Route element={<EnsureLoggedIn />}>
                    <Route element={<NavLayout />} path='/'>
                        <Route index element={<Navigate to="/infected" replace />} />
                        <Route path='/infected' element={<InfectedPage />} />
                        <Route path='/trojans' element={<Trojans />} />
                        <Route path='/infected/:iid' element={<InfectedControlRoom />} />
                    </Route>

                </Route>
                <Route path='/init' element={<Initialization />} />
                <Route path='/twofa' element={<TwoFA />} />
                <Route path='/login' element={<Login />} />
            </Routes>
        </BrowserRouter>
    </StrictMode>,
)
