// ── Mobile menu toggle ──
const menuToggle = document.getElementById('mobile-menu');
const navLinks   = document.querySelector('.nav-links');
if (menuToggle) {
    menuToggle.addEventListener('click', () => navLinks.classList.toggle('open'));
}

// ── Vacancy display (replaces api_vacancies.php call) ──
async function loadVacancies() {
    const displays = document.querySelectorAll('.vacancy-display');
    if (!displays.length) return;

    try {
        const res  = await fetch('/api/vacancies');
        const data = await res.json();

        displays.forEach(el => {
            const roomId = el.dataset.roomId;
            if (!roomId || !data[roomId]) return;

            const info = data[roomId];
            if (info.sold_out) {
                el.textContent = '❌ Sold Out';
                el.style.color = '#f87171';

                // disable book button in same card
                const btn = el.closest('.room-card')?.querySelector('.book-btn');
                if (btn) {
                    btn.disabled = true;
                    btn.style.opacity = '0.5';
                    btn.style.cursor  = 'not-allowed';
                }
            } else {
                el.textContent = `✅ ${info.available} room${info.available !== 1 ? 's' : ''} available`;
                el.style.color = '#34d399';
            }
        });
    } catch (err) {
        console.warn('Vacancy fetch failed:', err);
    }
}

// ── Check-out must be after check-in ──
const checkIn  = document.querySelector('input[name="checkIn"]');
const checkOut = document.querySelector('input[name="checkOut"]');
if (checkIn && checkOut) {
    checkIn.addEventListener('change', () => {
        checkOut.min = checkIn.value;
        if (checkOut.value && checkOut.value <= checkIn.value) checkOut.value = '';
    });
}

// ── Smooth scroll for anchor links ──
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', e => {
        const target = document.querySelector(anchor.getAttribute('href'));
        if (target) {
            e.preventDefault();
            target.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    });
});

// ── Navbar scroll shadow ──
window.addEventListener('scroll', () => {
    const nav = document.querySelector('.navbar');
    if (nav) nav.style.boxShadow = window.scrollY > 40 ? '0 4px 30px rgba(0,0,0,0.5)' : 'none';
});

// ── Init ──
document.addEventListener('DOMContentLoaded', loadVacancies);
