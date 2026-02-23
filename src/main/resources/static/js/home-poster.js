
// Nút sang trang poster
let slides = document.querySelectorAll('.slide');
let current = 0;

function showSlide(index) {
    slides.forEach((s, i) => s.classList.remove('active'));
    slides[index].classList.add('active');
}

function nextSlide() {
    current = (current + 1) % slides.length;
    showSlide(current);
}

function prevSlide() {
    current = (current - 1 + slides.length) % slides.length;
    showSlide(current);
}

setInterval(nextSlide, 4000);


// highlight Truyện update
document.querySelectorAll(".tabs .tab").forEach(tab => {
    tab.addEventListener("click", function(e) {
        e.preventDefault();
        document.querySelectorAll(".tabs .tab").forEach(t => t.classList.remove("active"));
        this.classList.add("active");
    });
});


