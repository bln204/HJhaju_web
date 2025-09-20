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
