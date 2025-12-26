package com.whoami.servlet;

import com.whoami.dao.*;
import com.whoami.model.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/events")
public class EventsServlet extends HttpServlet {
    private EventDAO eventDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        eventDAO = new EventDAO();
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");

        // Get categories for the form
        List<Category> categories = categoryDAO.getUserCategories(userId);
        request.setAttribute("categories", categories);
        request.setAttribute("userName", userName);

        request.getRequestDispatcher("events.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        try {
            // Parse form data
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            int categoryId = Integer.parseInt(request.getParameter("category"));
            String eventDateStr = request.getParameter("eventDate");
            String eventTimeStr = request.getParameter("eventTime");

            // Validate required fields
            if (title == null || title.trim().isEmpty() ||
                eventDateStr == null || eventDateStr.trim().isEmpty()) {
                request.setAttribute("error", "Title and date are required.");
                doGet(request, response);
                return;
            }

            // Parse date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            Date eventDate = new Date(dateFormat.parse(eventDateStr).getTime());
            Time eventTime = null;
            if (eventTimeStr != null && !eventTimeStr.trim().isEmpty()) {
                eventTime = new Time(timeFormat.parse(eventTimeStr).getTime());
            }

            // Create event
            Event event = new Event(userId, categoryId, title.trim(),
                                  description != null ? description.trim() : "",
                                  eventDate, eventTime);

            int eventId = eventDAO.createEvent(event);
            if (eventId > 0) {
                response.sendRedirect("today");
            } else {
                request.setAttribute("error", "Failed to create event. Please try again.");
                doGet(request, response);
            }

        } catch (ParseException e) {
            request.setAttribute("error", "Invalid date or time format.");
            doGet(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid category selected.");
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An unexpected error occurred. Please try again.");
            doGet(request, response);
        }
    }
}