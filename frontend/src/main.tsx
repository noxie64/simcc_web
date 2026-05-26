import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { BrowserRouter, Route, Routes } from 'react-router'
import NavLayout from './components/NavLayout'
import {Initialization} from "./pages/Initialization.tsx";
import {TwoFA} from "./pages/TwoFA.tsx";
import {Login} from "./pages/Login.tsx";
import {Dashboard} from "./pages/Dashboard.tsx";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
        <Routes>
            <Route element={<NavLayout />}>
                <Route path='/dashboard' element={<Dashboard />} />
            </Route>
            <Route path= '/init' element={<Initialization/>}/>
            <Route path= '/twofa' element={<TwoFA/>}/>
            <Route path= '/login' element={<Login/>}/>
        </Routes>
    </BrowserRouter>
  </StrictMode>,
)
