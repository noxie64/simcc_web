import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { BrowserRouter, Navigate, Route, Routes } from 'react-router'
import NavLayout from './components/NavLayout'
import { Initialization } from "./pages/Initialization.tsx";
import { TwoFA } from "./pages/TwoFA.tsx";
import { Login } from "./pages/Login.tsx";
import Settings from './pages/Settings.tsx'
import Trojans from './pages/Trojans.tsx'
import {InfectedPage} from "./pages/Infected.tsx"
import {InfectedWorkSpace} from "./pages/InfectedWorkSpace.tsx";

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <BrowserRouter>
            <Routes>
                <Route element={<NavLayout />} path='/'>
                    <Route index element={<Navigate to="/infected" replace />} />
                    <Route path='/infected' element={<InfectedPage />} />
                    <Route path='/settings' element={<Settings />} />
                    <Route path='/trojans' element={<Trojans />} />
                    <Route path='/infectedWorkspace/:id' element={<InfectedWorkSpace/>}/>
                </Route>
                <Route path='/init' element={<Initialization />} />
                <Route path='/twofa' element={<TwoFA />} />
                <Route path='/login' element={<Login />} />
            </Routes>
        </BrowserRouter>
    </StrictMode>,
)
