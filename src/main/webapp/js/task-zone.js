// Initialize edit task buttons with event delegation
document.addEventListener('DOMContentLoaded', function() {
    // Add event listeners for dynamically created edit task buttons
    document.addEventListener('click', function(e) {
        if (e.target.closest('.edit-task-btn')) {
            const btn = e.target.closest('.edit-task-btn');
            openEditTaskModal(
                btn.dataset.taskId,
                btn.dataset.title,
                btn.dataset.description,
                btn.dataset.type,
                btn.dataset.category,
                btn.dataset.date,
                btn.dataset.time
            );
        }
    });
});

function openAddTaskModal() {
    document.getElementById('addTaskModal').style.display = 'flex';
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('taskDate').value = today;
}

function closeAddTaskModal() {
    document.getElementById('addTaskModal').style.display = 'none';
    document.getElementById('title').value = '';
    document.getElementById('description').value = '';
    document.getElementById('taskType').value = 'SPECIAL';
    document.getElementById('taskDate').value = '';
    document.getElementById('taskTime').value = '';
}

function toggleDateTimeFields() {
    const taskType = document.getElementById('taskType').value;
    const dateTimeFields = document.getElementById('dateTimeFields');
    
    if (taskType === 'ROUTINE') {
        dateTimeFields.style.display = 'none';
        document.getElementById('taskDate').removeAttribute('required');
    } else {
        dateTimeFields.style.display = 'block';
        document.getElementById('taskDate').setAttribute('required', 'required');
    }
}

function openEditTaskModal(taskId, title, description, taskType, categoryId, taskDate, taskTime) {
    document.getElementById('editTaskModal').style.display = 'flex';
    document.getElementById('editTaskId').value = taskId;
    document.getElementById('editTitle').value = title;
    document.getElementById('editDescription').value = description;
    document.getElementById('editTaskType').value = taskType;
    document.getElementById('editCategoryId').value = categoryId;
    document.getElementById('editTaskDate').value = taskDate;
    document.getElementById('editTaskTime').value = taskTime;
    
    toggleEditDateTimeFields();
}

function closeEditTaskModal() {
    document.getElementById('editTaskModal').style.display = 'none';
}

function toggleEditDateTimeFields() {
    const taskType = document.getElementById('editTaskType').value;
    const dateTimeFields = document.getElementById('editDateTimeFields');
    
    if (taskType === 'ROUTINE') {
        dateTimeFields.style.display = 'none';
        document.getElementById('editTaskDate').removeAttribute('required');
    } else {
        dateTimeFields.style.display = 'block';
        document.getElementById('editTaskDate').setAttribute('required', 'required');
    }
}

window.onclick = function(event) {
    const addModal = document.getElementById('addTaskModal');
    const editModal = document.getElementById('editTaskModal');
    
    if (event.target === addModal) {
        closeAddTaskModal();
    }
    if (event.target === editModal) {
        closeEditTaskModal();
    }
}

document.addEventListener('DOMContentLoaded', function() {
    toggleDateTimeFields();
});
