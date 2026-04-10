import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { BrowserRouter, Route, Routes } from 'react-router'
import NavLayout from './components/NavLayout'
import Dummy from './pages/Dummy'
import {Initialization} from "./pages/Initialization.tsx";
import {TwoFA} from "./pages/TwoFA.tsx";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
        <Routes>
            <Route element={<NavLayout />}>
                <Route path='/dummy' element={<Dummy />} />
            </Route>
            <Route path= '/init' element={<Initialization/>}/>
            <Route path= '/twofa' element={<TwoFA/>}/>
        </Routes>
    </BrowserRouter>
  </StrictMode>,
)
