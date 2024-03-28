import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';


const Layout = () => {
    return (
        <div>
            <Sidebar />
            <div>
                <Outlet />
            </div>
        </div>
        );
};

export default Layout;
