
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

//search
$(document).ready(function() {
    $("#searchBox").on("input", function() {
        let keyword = $(this).val();

        if (keyword.length < 2) {
            $("#suggestions").empty();
            return;
        }

        $.ajax({
            url: "/home/suggest",
            type: "GET",
            data: { keyword: keyword },
            success: function(data) {
                let list = "";
                data.forEach(c => {
                    list += `<li><a href="/home/${c.id}">${c.name}</a></li>`;
                });
                $("#suggestions").html(list);
            },
            error: function() {
                $("#suggestions").html("<li style='color:red'>Error loading</li>");
            }
        });
    });

    // Khi click ra ngoài thì ẩn gợi ý
    $(document).click(function(e) {
        if (!$(e.target).closest("#searchBox").length) {
            $("#suggestions").empty();
        }
    });
});