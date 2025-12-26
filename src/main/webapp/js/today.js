function toggleTodayItem(type, id, complete) {
    const form = document.createElement('form');
    form.method = 'POST';
    
    if (type === 'habit') {
        form.action = 'habits';
        
        const actionInput = document.createElement('input');
        actionInput.type = 'hidden';
        actionInput.name = 'action';
        actionInput.value = complete ? 'complete' : 'uncomplete';
        
        const habitInput = document.createElement('input');
        habitInput.type = 'hidden';
        habitInput.name = 'habitId';
        habitInput.value = id;
        
        const dateInput = document.createElement('input');
        dateInput.type = 'hidden';
        dateInput.name = 'date';
        dateInput.value = new URLSearchParams(window.location.search).get('date') || new Date().toISOString().split('T')[0];
        
        form.appendChild(actionInput);
        form.appendChild(habitInput);
        form.appendChild(dateInput);
    } else if (type === 'task') {
        form.action = 'task-zone';
        
        const actionInput = document.createElement('input');
        actionInput.type = 'hidden';
        actionInput.name = 'action';
        actionInput.value = complete ? 'complete' : 'uncomplete';
        
        const taskInput = document.createElement('input');
        taskInput.type = 'hidden';
        taskInput.name = 'taskId';
        taskInput.value = id;
        
        form.appendChild(actionInput);
        form.appendChild(taskInput);
    } else if (type === 'event') {
        form.action = 'today';
        
        const actionInput = document.createElement('input');
        actionInput.type = 'hidden';
        actionInput.name = 'action';
        actionInput.value = 'toggle';
        
        const typeInput = document.createElement('input');
        typeInput.type = 'hidden';
        typeInput.name = 'type';
        typeInput.value = 'event';
        
        const idInput = document.createElement('input');
        idInput.type = 'hidden';
        idInput.name = 'id';
        idInput.value = id;
        
        const completeInput = document.createElement('input');
        completeInput.type = 'hidden';
        completeInput.name = 'complete';
        completeInput.value = complete;
        
        form.appendChild(actionInput);
        form.appendChild(typeInput);
        form.appendChild(idInput);
        form.appendChild(completeInput);
    }
    
    document.body.appendChild(form);
    form.submit();
}

function openItemDetails(type, id) {
    if (type === 'habit') {
        window.location.href = 'habits';
    } else if (type === 'task') {
        window.location.href = 'task-zone';
    } else if (type === 'event') {
        console.log('Open event:', id);
    }
}

let fabMenuOpen = false;

function toggleFabMenu() {
    const fabMenu = document.getElementById('fabMenu');
    fabMenuOpen = !fabMenuOpen;
    
    if (fabMenuOpen) {
        fabMenu.classList.add('active');
    } else {
        fabMenu.classList.remove('active');
    }
}

function showAddEventModal() {
    toggleFabMenu();
    console.log('Add event');
}

function openEventModal() {
    const modal = document.getElementById('addEventModal');
    modal.classList.add('active');
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.classList.remove('active');
}

document.addEventListener('click', function(e) {
    const fab = document.querySelector('.fab');
    const fabMenu = document.getElementById('fabMenu');
    
    if (fabMenuOpen && !fab.contains(e.target) && !fabMenu.contains(e.target)) {
        toggleFabMenu();
    }
});

// Modal event listeners
document.addEventListener('DOMContentLoaded', function() {
    const addEventModal = document.getElementById('addEventModal');
    if (addEventModal) {
        addEventModal.addEventListener('click', function(e) {
            if (e.target === addEventModal) {
                closeModal('addEventModal');
            }
        });
        
        // Close modal on escape key
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape' && addEventModal.classList.contains('active')) {
                closeModal('addEventModal');
            }
        });
    }
});
