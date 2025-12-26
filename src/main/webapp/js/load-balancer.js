// Initialize event listeners when DOM loads
document.addEventListener('DOMContentLoaded', function() {
    // Edit button handlers using event delegation
    document.addEventListener('click', function(e) {
        if (e.target.closest('.edit-btn')) {
            const btn = e.target.closest('.edit-btn');
            openEditLoadModal(
                btn.dataset.entryId,
                btn.dataset.subject,
                btn.dataset.difficulty,
                btn.dataset.urgency,
                btn.dataset.effort,
                btn.dataset.date
            );
        }
        // Delete button handlers
        if (e.target.closest('.delete-btn')) {
            const btn = e.target.closest('.delete-btn');
            deleteLoad(btn.dataset.entryId);
        }
    });
});

function openAddLoadModal() {
    document.getElementById('modalTitle').textContent = 'Add Subject';
    document.getElementById('formAction').value = 'add';
    document.getElementById('entryId').value = '';
    document.getElementById('loadForm').reset();

    const today = new Date().toISOString().split('T')[0];
    document.getElementById('entryDate').value = today;

    document.getElementById('loadModal').style.display = 'flex';
    document.body.style.overflow = 'hidden'; // Prevent background scrolling
}

function openEditLoadModal(entryId, subjectName, difficulty, urgency, effort, entryDate) {
    document.getElementById('modalTitle').textContent = 'Edit Subject';
    document.getElementById('formAction').value = 'update';
    document.getElementById('entryId').value = entryId;
    document.getElementById('subjectName').value = subjectName;
    document.getElementById('difficulty').value = difficulty;
    document.getElementById('urgency').value = urgency;
    document.getElementById('effort').value = effort;
    document.getElementById('entryDate').value = entryDate;

    document.getElementById('loadModal').style.display = 'flex';
    document.body.style.overflow = 'hidden'; // Prevent background scrolling
}

function closeLoadModal() {
    document.getElementById('loadModal').style.display = 'none';
    document.body.style.overflow = ''; // Restore scrolling
}

function deleteLoad(entryId) {
    if (confirm('Remove this subject from your study load?')) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = 'load-balancer';

        const actionInput = document.createElement('input');
        actionInput.type = 'hidden';
        actionInput.name = 'action';
        actionInput.value = 'delete';

        const idInput = document.createElement('input');
        idInput.type = 'hidden';
        idInput.name = 'entryId';
        idInput.value = entryId;

        form.appendChild(actionInput);
        form.appendChild(idInput);
        document.body.appendChild(form);
        form.submit();
    }
}

// Close modal when clicking outside
window.onclick = function(event) {
    const modal = document.getElementById('loadModal');
    if (event.target === modal) {
        closeLoadModal();
    }
};

// Close modal on Escape key
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        const modal = document.getElementById('loadModal');
        if (modal.style.display === 'flex') {
            closeLoadModal();
        }
    }
});
