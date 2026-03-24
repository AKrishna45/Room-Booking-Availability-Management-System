document.addEventListener('DOMContentLoaded', () => {
    const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
    const currentPage = window.location.pathname;

    // --- 1. DYNAMIC NAVBAR ---
    const navLinks = document.querySelector('.nav-links');
    if (navLinks) {
        if (isLoggedIn) {
            navLinks.innerHTML = `
                <li><a href="index.html">Home</a></li>
                <li><a href="index.html#about">About</a></li>
                <li><a href="index.html#rooms">Rooms</a></li>
                <li><a href="my-booking.html">My Bookings</a></li>
                <li><a href="#" id="logoutBtn" class="btn-login">Logout</a></li>
            `;
            document.getElementById('logoutBtn').addEventListener('click', (e) => {
                e.preventDefault();
                localStorage.removeItem('isLoggedIn');
                window.location.href = 'index.html';
            });
        } else {
            navLinks.innerHTML = `
                <li><a href="index.html">Home</a></li>
                <li><a href="index.html#about">About</a></li>
                <li><a href="index.html#rooms">Rooms</a></li>
                <li><a href="login.html" class="btn-login">Login</a></li>
            `;
        }
    }

    // --- 2. ROUTE GUARD ---
    const protectedPages = ['my-booking.html', 'booking.html'];
    if (protectedPages.some(page => currentPage.includes(page)) && !isLoggedIn) {
        alert("Access Denied! Please login first.");
        window.location.href = 'login.html';
    }

    // --- 3. LOGIN LOGIC ---
    const loginForm = document.querySelector('.auth-container form');
    if (loginForm && currentPage.includes('login.html')) {
        loginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            localStorage.setItem('isLoggedIn', 'true');
            window.location.href = 'index.html';
        });
    }

    // --- 4. ROOM SELECTION (Home Page) ---
    const bookBtns = document.querySelectorAll('.book-btn');
    bookBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            if (!isLoggedIn) {
                alert("Please login to book a room.");
                window.location.href = 'login.html';
            } else {
                localStorage.setItem('selectedRoom', btn.dataset.room);
                window.location.href = 'booking.html';
            }
        });
    });

    // --- 5. BOOKING FORM (Booking Page) ---
    if (currentPage.includes('booking.html')) {
        const roomInput = document.getElementById('displayRoomName');
        const savedRoom = localStorage.getItem('selectedRoom');
        if (roomInput) roomInput.value = savedRoom || "Deluxe Room";

        const bForm = document.getElementById('bookingForm');
        bForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const newBooking = {
                roomName: roomInput.value,
                checkIn: bForm.querySelectorAll('input[type="date"]')[0].value,
                checkOut: bForm.querySelectorAll('input[type="date"]')[1].value,
                guests: bForm.querySelector('input[type="number"]').value,
                id: "SE-" + Math.floor(Math.random() * 9000),
                img: "https://images.unsplash.com/photo-1566665797739-1674de7a421a"
            };

            let history = JSON.parse(localStorage.getItem('bookingHistory')) || [];
            history.unshift(newBooking);
            localStorage.setItem('bookingHistory', JSON.stringify(history));
            
            alert("Booking Confirmed!");
            window.location.href = 'my-booking.html';
        });
    }

    // --- 6. RENDER HISTORY (My Bookings Page) ---
    const historyList = document.getElementById('bookingHistoryList');
    if (historyList && currentPage.includes('my-booking.html')) {
        const history = JSON.parse(localStorage.getItem('bookingHistory')) || [];
        
        if (history.length === 0) {
            historyList.innerHTML = `<p style="text-align:center; padding:50px;">No bookings found. <a href="index.html#rooms">Book one now!</a></p>`;
        } else {
            historyList.innerHTML = history.map((item, index) => `
                <div class="booking-card-horizontal">
                    <div class="booking-img"><img src="${item.img}"></div>
                    <div class="booking-details">
                        <div class="card-top">
                            <h3>${item.roomName}</h3>
                            <span class="status-badge confirmed">Confirmed</span>
                        </div>
                        <div class="booking-info-grid">
                            <div class="info-item"><small>Check-in</small><p>${item.checkIn}</p></div>
                            <div class="info-item"><small>Check-out</small><p>${item.checkOut}</p></div>
                            <div class="info-item"><small>ID</small><p>${item.id}</p></div>
                        </div>
                        <div class="card-footer">
                            <button class="btn-cancel" onclick="cancelBooking(${index})">Cancel Booking</button>
                        </div>
                    </div>
                </div>
            `).join('');
        }
    }
});

// GLOBAL FUNCTION FOR DELETION
window.cancelBooking = (index) => {
    if (confirm("Are you sure you want to cancel?")) {
        let history = JSON.parse(localStorage.getItem('bookingHistory'));
        history.splice(index, 1);
        localStorage.setItem('bookingHistory', JSON.stringify(history));
        window.location.reload();
    }
};