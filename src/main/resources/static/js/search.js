const searchInput = document.getElementById('searchInput');
const autocompleteDropdown = document.getElementById('autocompleteDropdown');
const autocompleteResults = document.getElementById('autocompleteResults');
const autocompleteHeader = document.getElementById('autocompleteHeader');

let searchTimeout;

// Event listeners
searchInput.addEventListener('input', handleSearch);
searchInput.addEventListener('focus', handleFocus);
searchInput.addEventListener('blur', handleBlur);

// Handle search input
function handleSearch(e) {
    const query = e.target.value.trim();

    clearTimeout(searchTimeout);

    if (query.length < 2) {
        hideDropdown();
        return;
    }

    showLoading();

    searchTimeout = setTimeout(() => {
        searchMangas(query);
    }, 300);
}

// Handle focus
function handleFocus() {
    const query = searchInput.value.trim();
    if (query.length > 1) {
        searchMangas(query);
    }
}

// Handle blur
function handleBlur() {
    setTimeout(() => {
        hideDropdown();
    }, 200);
}

// Search function (API call to backend)
function searchMangas(query) {
    console.log("Search event triggered:", this.value);
    fetch(`/api/search/suggestions?query=${encodeURIComponent(query)}&limit=10`)
        .then(response => response.json())
        .then(results => displayResults(results, query))
        .catch(error => {
            console.error("Search error:", error);
            showNoResults(query);
        });
}

// Display search results
function displayResults(results, query) {
    if (results.length === 0) {
        showNoResults(query);
        return;
    }

    autocompleteHeader.textContent = `Tìm thấy ${results.length} kết quả cho "${query}"`;

    const html = results.map(comic => `
        <div class="autocomplete-item" onclick="selectManga('${comic.slug}', '${comic.name}')">
            <div class="manga-thumbnail">
                <img src="${comic.thumbImage}" alt="${comic.name}" onerror="this.style.display='none'">
            </div>
            <div class="manga-info">
                <div class="manga-title">${highlightText(comic.name, query)}</div>
                <div class="manga-meta">
                    <div class="manga-genre">${comic.category}</div>
                </div>
            </div>
        </div>
    `).join('');

    autocompleteResults.innerHTML = html;
    showDropdown();
}

// Show loading state
function showLoading() {
    autocompleteHeader.textContent = 'Đang tìm kiếm...';
    autocompleteResults.innerHTML = `
        <div class="loading">
            <div class="loading-spinner"></div>
            Đang tải kết quả...
        </div>
    `;
    showDropdown();
}

// Show no results
function showNoResults(query) {
    autocompleteHeader.textContent = `Không tìm thấy kết quả cho "${query}"`;
    autocompleteResults.innerHTML = `
        <div class="no-results">
            <div class="no-results-icon">😞</div>
            <div>Không tìm thấy truyện nào phù hợp</div>
            <div style="margin-top: 5px; font-size: 11px; color: rgba(255,255,255,0.4);">
                Thử tìm kiếm với từ khóa khác
            </div>
        </div>
    `;
    showDropdown();
}

// Highlight matching text
function highlightText(text, query) {
    const regex = new RegExp(`(${escapeRegExp(query)})`, 'gi');
    return text.replace(regex, '<span style="background: rgba(255,107,107,0.3); color: #ff6b6b; font-weight: bold;">$1</span>');
}

function escapeRegExp(string) {
    return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

// Show / Hide dropdown
function showDropdown() {
    autocompleteDropdown.classList.add('active');
}
function hideDropdown() {
    autocompleteDropdown.classList.remove('active');
}

// Select manga
function selectManga(slug, name) {
    searchInput.value = name;
    hideDropdown();
    window.location.href = `/${slug}`;
}

// Perform search manually (search button)
function performSearch() {
    const query = searchInput.value.trim();
    if (query.length >= 2) {
        window.location.href = `/search?query=${encodeURIComponent(query)}`;
    }
}

searchInput.addEventListener("keydown", function(event) {
    if (event.key === "Enter") {
        event.preventDefault(); // Ngăn form reload (nếu có)
        performSearch();
    }
});


// Close dropdown when clicking outside
document.addEventListener('click', function(e) {
    if (!e.target.closest('.search-container')) {
        hideDropdown();
    }
});

// Keyboard navigation
let currentSelection = -1;
searchInput.addEventListener('keydown', function(e) {
    const items = document.querySelectorAll('.autocomplete-item');

    switch(e.key) {
        case 'ArrowDown':
            e.preventDefault();
            currentSelection = Math.min(currentSelection + 1, items.length - 1);
            updateSelection(items);
            break;

        case 'ArrowUp':
            e.preventDefault();
            currentSelection = Math.max(currentSelection - 1, -1);
            updateSelection(items);
            break;

        case 'Enter':
            e.preventDefault();
            if (currentSelection >= 0 && items[currentSelection]) {
                items[currentSelection].click();
            } else {
                performSearch();
            }
            break;

        case 'Escape':
            hideDropdown();
            searchInput.blur();
            break;
    }
});

function updateSelection(items) {
    items.forEach((item, index) => {
        if (index === currentSelection) {
            item.style.background = 'rgba(255,107,107,0.2)';
        } else {
            item.style.background = '';
        }
    });
}
