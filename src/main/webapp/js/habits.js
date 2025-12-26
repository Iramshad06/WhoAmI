function showAddHabitModal() {
    const modal = document.getElementById('addHabitModal');
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.classList.remove('active');
    document.body.style.overflow = '';
}

function editHabit(habitId) {
    console.log('Edit habit:', habitId);
}

function toggleHabitDay(habitId, date, isCompleted) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = 'habits';
    
    const actionInput = document.createElement('input');
    actionInput.type = 'hidden';
    actionInput.name = 'action';
    actionInput.value = isCompleted ? 'uncomplete' : 'complete';
    
    const habitInput = document.createElement('input');
    habitInput.type = 'hidden';
    habitInput.name = 'habitId';
    habitInput.value = habitId;
    
    const dateInput = document.createElement('input');
    dateInput.type = 'hidden';
    dateInput.name = 'date';
    dateInput.value = date;
    
    form.appendChild(actionInput);
    form.appendChild(habitInput);
    form.appendChild(dateInput);
    
    document.body.appendChild(form);
    form.submit();
}

document.addEventListener('click', function(e) {
    const modal = document.getElementById('addHabitModal');
    if (e.target === modal) {
        closeModal('addHabitModal');
    }
});

document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        closeModal('addHabitModal');
    }
});
