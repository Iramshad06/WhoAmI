<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<nav class="navbar">
    <div class="container navbar-content">
        <a href="/dashboard" class="navbar-brand">WhoAmI</a>
        <div class="navbar-menu">
            <a href="/dashboard" class="navbar-link${activePage eq 'dashboard' ? ' active' : ''}">Dashboard</a>
            <div class="navbar-dropdown">
                <a href="/today" class="navbar-dropdown-toggle${activeSection eq 'tasks' ? ' active' : ''}" style="text-decoration: none; color: inherit; display: flex; align-items: center; gap: 8px;">
                    My Tasks
                    <svg class="navbar-dropdown-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <polyline points="6 9 12 15 18 9"></polyline>
                    </svg>
                </a>
                <div class="navbar-dropdown-menu">
                    <a href="/today" class="navbar-dropdown-item${activePage eq 'today' ? ' active' : ''}">Today</a>
                    <a href="/habits" class="navbar-dropdown-item${activePage eq 'habits' ? ' active' : ''}">Habits</a>
                    <a href="/load-balancer" class="navbar-dropdown-item${activePage eq 'load-balancer' ? ' active' : ''}">Load Balancer</a>
                </div>
            </div>
            <div class="navbar-dropdown">
                <button type="button" class="navbar-dropdown-toggle${activeSection eq 'mind' ? ' active' : ''}">
                    My Mind
                    <svg class="navbar-dropdown-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <polyline points="6 9 12 15 18 9"></polyline>
                        </svg>
                </button>
                <div class="navbar-dropdown-menu">
                    <a href="/mind-checkin" class="navbar-dropdown-item${activePage eq 'mind-checkin' ? ' active' : ''}">Mind Check-In</a>
                    <a href="/study-zone" class="navbar-dropdown-item${activePage eq 'study-zone' ? ' active' : ''}">Study Zone</a>
                </div>
            </div>
            <div class="navbar-dropdown">
                <button type="button" class="navbar-dropdown-toggle${activeSection eq 'growth' ? ' active' : ''}">
                    My Growth
                    <svg class="navbar-dropdown-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <polyline points="6 9 12 15 18 9"></polyline>
                    </svg>
                </button>
                <div class="navbar-dropdown-menu">
                    <a href="/growth-timeline" class="navbar-dropdown-item${activePage eq 'growth-timeline' ? ' active' : ''}">Growth Timeline</a>
                    <a href="/insights" class="navbar-dropdown-item${activePage eq 'insights' ? ' active' : ''}">Insights</a>
                    <a href="/reflection" class="navbar-dropdown-item${activePage eq 'reflection' ? ' active' : ''}">Reflection</a>
                </div>
            </div>
        </div>
        <div class="navbar-user">
            <c:if test="${not empty userName}">
                <span class="user-name">${userName}</span>
            </c:if>
            <button class="theme-toggle" onclick="toggleTheme()" aria-label="Toggle theme">
                <svg id="theme-icon" class="theme-icon" viewBox="0 0 24 24" fill="currentColor">
                    <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
                </svg>
            </button>
            <a href="/logout" class="btn btn-secondary" style="padding: 8px 16px; font-size: 14px;">Logout</a>
        </div>
    </div>
</nav>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // Make dropdowns work on click for mobile/touch devices
    const dropdowns = document.querySelectorAll('.navbar-dropdown');
    dropdowns.forEach(dropdown => {
        const toggle = dropdown.querySelector('.navbar-dropdown-toggle');
        const menu = dropdown.querySelector('.navbar-dropdown-menu');
        
        toggle.addEventListener('click', function(e) {
            e.preventDefault();
            // Close other dropdowns
            document.querySelectorAll('.navbar-dropdown-menu').forEach(m => {
                if (m !== menu) {
                    m.style.opacity = '0';
                    m.style.visibility = 'hidden';
                    m.style.transform = 'translateY(-10px)';
                }
            });
            // Toggle this dropdown
            if (menu.style.opacity === '1') {
                menu.style.opacity = '0';
                menu.style.visibility = 'hidden';
                menu.style.transform = 'translateY(-10px)';
            } else {
                menu.style.opacity = '1';
                menu.style.visibility = 'visible';
                menu.style.transform = 'translateY(0)';
            }
        });
    });
    
    // Close dropdowns when clicking outside
    document.addEventListener('click', function(e) {
        if (!e.target.closest('.navbar-dropdown')) {
            document.querySelectorAll('.navbar-dropdown-menu').forEach(menu => {
                menu.style.opacity = '0';
                menu.style.visibility = 'hidden';
                menu.style.transform = 'translateY(-10px)';
            });
        }
    });
});
</script>
