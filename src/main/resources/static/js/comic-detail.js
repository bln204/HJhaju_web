//comic detail
document.addEventListener("DOMContentLoaded", function () {
    const btn = document.getElementById("seeMoreBtn");
    const rows = document.querySelectorAll("tbody tr");

    btn.addEventListener("click", function () {
        if (btn.getAttribute("data-expanded") === "true") {
            // Thu gọn: ẩn từ chương thứ 6 trở đi
            rows.forEach((row, index) => {
                if (index >= 10) row.classList.add("hidden");
            });
            btn.textContent = "Hiện Tất Cả";
            btn.setAttribute("data-expanded", "false");
        } else {
            // Mở rộng: hiện tất cả
            rows.forEach(row => row.classList.remove("hidden"));
            btn.textContent = "Thu gọn";
            btn.setAttribute("data-expanded", "true");
        }
    });
});

function getAvailableChapters() {
    const rows = document.querySelectorAll('tr[data-chapter]');
    return Array.from(rows).map(row => row.dataset.chapter.trim());
}



function goToChapter() {
    const input = document.getElementById('chapterNumberInput');
    const chapterNumber = input.value.trim();
    const comicSlug = input.dataset.comicSlug;

    if (!chapterNumber || chapterNumber < 0) {
        showError('Vui lòng nhập số chương hợp lệ!');
        input.focus();
        return;
    }

    const availableChapters = getAvailableChapters();

    if (!availableChapters.includes(chapterNumber)) {
        showError(`Chương ${chapterNumber} không tồn tại!`);
        input.focus();
        return;
    }

    window.location.href = `/${comicSlug}/chuong-${chapterNumber}`;
}

function showError(message) {
    const errorDiv = document.getElementById('errorMessage');
    errorDiv.textContent = message;
    errorDiv.classList.add('show');

    setTimeout(() => {
        errorDiv.classList.remove('show');
    }, 3000);
}

// Enter key support
document.getElementById('chapterNumberInput').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        goToChapter();
    }
});

document.getElementById('chapterNumberInput').addEventListener('input', function(e) {
    if (this.value < 0) {
        this.value = Math.abs(this.value);
    }
});

const input = document.getElementById('chapterNumberInput');

input.addEventListener('input', function () {
    // Loại bỏ mọi ký tự không phải số hoặc dấu chấm
    this.value = this.value.replace(/[^0-9.]/g, '');

    // Chỉ cho phép tối đa 1 dấu chấm
    const parts = this.value.split('.');
    if (parts.length > 2) {
        this.value = parts[0] + '.' + parts[1];
    }
});