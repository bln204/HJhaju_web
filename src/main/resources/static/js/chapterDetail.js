// Show Chapter Selector function
function showChapterSelector() {
    toggleChapterSelector();
}

// Toggle Chapter Selector
function toggleChapterSelector() {
    const chapterSelector = document.getElementById('chapterSelector');
    const chapterOverlay = document.getElementById('chapterOverlay');

    chapterSelector.classList.toggle('active');
    chapterOverlay.classList.toggle('active');

    // Focus vào search box khi mở
    if (chapterSelector.classList.contains('active')) {
        setTimeout(() => {
            document.getElementById('chapterSearchInput').focus();
        }, 400);
    }
}

// Search Chapters function
function searchChapters(searchTerm) {
    const chapterList = document.getElementById('chapterList');
    const items = chapterList.querySelectorAll('.chapter-list-item');

    items.forEach(item => {
        const chapterName = item.querySelector('.chapter-name').textContent.toLowerCase();
        if (chapterName.includes(searchTerm.toLowerCase()) || searchTerm === '') {
            item.style.display = 'flex';
        } else {
            item.style.display = 'none';
        }
    });
}

// Select Chapter function
function selectChapter(chapterNumber) {
    // Update current chapter in sidebar
    const currentChapterSpan = document.querySelector('.sidebar-item.active span');
    if (currentChapterSpan) {
        currentChapterSpan.textContent = `Chương ${chapterNumber}`;
    }

    // Update breadcrumb
    const breadcrumbSpan = document.querySelector('.breadcrumb span:last-child');
    if (breadcrumbSpan) {
        breadcrumbSpan.textContent = `Chương ${chapterNumber}`;
    }

    // Close chapter selector
    toggleChapterSelector();
}

// Toggle Sidebar
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('overlay');
    const content = document.getElementById('mainContent');

    sidebar.classList.toggle('active');
    overlay.classList.toggle('active');

    if (window.innerWidth > 768) {
        content.classList.toggle('with-sidebar');
    }
}


// Khi load trang, disable nút nếu ở đầu/cuối
window.addEventListener("DOMContentLoaded", () => {
    const nav = document.getElementById("chapterNav");
    const current = nav.dataset.currentChapter;
    const chapters = nav.dataset.chapters.replace(/[\[\]\s]/g, '').split(",");
    const slug = nav.dataset.comicSlug;

    const prevBtn = document.getElementById("prevBtn");
    const nextBtn = document.getElementById("nextBtn");

    const currentIndex = chapters.indexOf(current);

    if (currentIndex === 0) {
        prevBtn.disabled = true;
        prevBtn.style.opacity = "0.5";
    } else {
        prevBtn.onclick = () => {
            window.location.href = `/${slug}/chuong-${chapters[currentIndex - 1]}`;
        };
    }

    if (currentIndex === chapters.length - 1) {
        nextBtn.disabled = true;
        nextBtn.style.opacity = "0.5";
    } else {
        nextBtn.onclick = () => {
            window.location.href = `/${slug}/chuong-${chapters[currentIndex + 1]}`;
        };
    }
});

window.addEventListener("DOMContentLoaded", () => {
    const nav = document.getElementById("chapterNav");
    const current = nav.dataset.currentChapter;
    const chapters = nav.dataset.chapters.replace(/[\[\]\s]/g, '').split(",");
    const slug = nav.dataset.comicSlug;

    const prevBtn = document.getElementById("prevBtnBottom");
    const nextBtn = document.getElementById("nextBtnBottom");

    const currentIndex = chapters.indexOf(current);

    if (currentIndex === 0) {
        prevBtn.disabled = true;
        prevBtn.style.opacity = "0.5";
    } else {
        prevBtn.onclick = () => {
            window.location.href = `/${slug}/chuong-${chapters[currentIndex - 1]}`;
        };
    }

    if (currentIndex === chapters.length - 1) {
        nextBtn.disabled = true;
        nextBtn.style.opacity = "0.5";
    } else {
        nextBtn.onclick = () => {
            window.location.href = `/${slug}/chuong-${chapters[currentIndex + 1]}`;
        };
    }
});


function showChapterList() {
    toggleSidebar();
}

function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

// Close sidebar when clicking on sidebar items
document.addEventListener('DOMContentLoaded', function() {
    const sidebarItems = document.querySelectorAll('.sidebar-item');
    sidebarItems.forEach(item => {
        item.addEventListener('click', function(e) {
            if (this.onclick !== scrollToTop) {
                console.log('Clicked:', this.textContent.trim());
            }
        });
    });
});

// Handle responsive behavior
window.addEventListener('resize', function() {
    const sidebar = document.getElementById('sidebar');
    const content = document.getElementById('mainContent');

    if (window.innerWidth <= 768) {
        content.classList.remove('with-sidebar');
    } else if (sidebar.classList.contains('active')) {
        content.classList.add('with-sidebar');
    }
});

// Close sidebar with Escape key
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        const sidebar = document.getElementById('sidebar');
        const chapterSelector = document.getElementById('chapterSelector');

        if (chapterSelector.classList.contains('active')) {
            toggleChapterSelector();
        } else if (sidebar.classList.contains('active')) {
            toggleSidebar();
        }
    }
});

// Clear search when chapter selector closes
document.addEventListener('click', function(e) {
    const chapterSelector = document.getElementById('chapterSelector');
    if (!chapterSelector.classList.contains('active')) {
        document.getElementById('chapterSearchInput').value = '';
        searchChapters(''); // Reset search
    }
});

let lastScrollTop = 0;
const navbar = document.querySelector('.navbar');

window.addEventListener('scroll', function() {
    const scrollTop = window.scrollY || document.documentElement.scrollTop;

    if (scrollTop > lastScrollTop) {
        // Kéo xuống -> ẩn navbar
        navbar.classList.add('hidden');
    } else {
        // Kéo lên -> hiện navbar
        navbar.classList.remove('hidden');
    }

    lastScrollTop = scrollTop <= 0 ? 0 : scrollTop;
});