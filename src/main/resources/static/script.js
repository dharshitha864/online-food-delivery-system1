/**
 * QuickBite - Online Food Delivery System
 * Frontend Application Logic
 */

// ===================================================================
// DEFAULT SAMPLE DATA (Used directly or synced with Java Backend)
// ===================================================================

const DEFAULT_RESTAURANTS = [
    {
        id: 1,
        name: "Pizza Palace",
        cuisine: "Italian & Pizzeria",
        rating: 4.8,
        deliveryTime: "25-30 mins",
        bannerClass: "banner-pizza",
        emoji: "🍕"
    },
    {
        id: 2,
        name: "Burger House",
        cuisine: "American Burgers & Grill",
        rating: 4.6,
        deliveryTime: "20-25 mins",
        bannerClass: "banner-burger",
        emoji: "🍔"
    },
    {
        id: 3,
        name: "South Indian Kitchen",
        cuisine: "Authentic South Indian",
        rating: 4.9,
        deliveryTime: "15-20 mins",
        bannerClass: "banner-south",
        emoji: "🥞"
    },
    {
        id: 4,
        name: "Chinese Wok",
        cuisine: "Pan-Asian & Chinese",
        rating: 4.5,
        deliveryTime: "30-35 mins",
        bannerClass: "banner-chinese",
        emoji: "🍜"
    },
    {
        id: 5,
        name: "Healthy Bites",
        cuisine: "Salads, Bowls & Organic",
        rating: 4.7,
        deliveryTime: "15-25 mins",
        bannerClass: "banner-healthy",
        emoji: "🥗"
    }
];

const DEFAULT_FOODS = [
    // Pizza Palace
    {
        id: 101,
        name: "Margherita Pizza",
        category: "Pizza",
        price: 12.99,
        restaurantName: "Pizza Palace",
        restaurantId: 1,
        description: "Classic stone-baked pizza with San Marzano tomatoes, fresh mozzarella, and aromatic basil.",
        emoji: "🍕"
    },
    {
        id: 102,
        name: "Pepperoni Feast",
        category: "Pizza",
        price: 15.99,
        restaurantName: "Pizza Palace",
        restaurantId: 1,
        description: "Loaded double pepperoni, melted mozzarella cheese, and authentic herb marinara.",
        emoji: "🍕"
    },
    {
        id: 103,
        name: "Garlic Herb Breadsticks",
        category: "Sides",
        price: 6.49,
        restaurantName: "Pizza Palace",
        restaurantId: 1,
        description: "Warm oven-baked buttery breadsticks served with marinara dipping sauce.",
        emoji: "🥖"
    },
    {
        id: 104,
        name: "Classic Italian Tiramisu",
        category: "Desserts",
        price: 5.99,
        restaurantName: "Pizza Palace",
        restaurantId: 1,
        description: "Coffee-dipped ladyfingers layered with rich mascarpone and cocoa powder.",
        emoji: "🍰"
    },

    // Burger House
    {
        id: 201,
        name: "Classic Cheeseburger",
        category: "Burgers",
        price: 9.99,
        restaurantName: "Burger House",
        restaurantId: 2,
        description: "Prime grilled beef patty, cheddar cheese, crisp lettuce, tomato, and house burger sauce.",
        emoji: "🍔"
    },
    {
        id: 202,
        name: "Smoky BBQ Bacon Burger",
        category: "Burgers",
        price: 13.49,
        restaurantName: "Burger House",
        restaurantId: 2,
        description: "Crispy applewood smoked bacon, caramelized onions, melted cheddar, and bold BBQ sauce.",
        emoji: "🍔"
    },
    {
        id: 203,
        name: "Crispy Truffle Fries",
        category: "Sides",
        price: 5.49,
        restaurantName: "Burger House",
        restaurantId: 2,
        description: "Golden crispy skin-on fries tossed with parmesan cheese and fragrant black truffle oil.",
        emoji: "🍟"
    },
    {
        id: 204,
        name: "Thick Chocolate Milkshake",
        category: "Beverages",
        price: 4.99,
        restaurantName: "Burger House",
        restaurantId: 2,
        description: "Ultra-thick hand-spun chocolate shake crowned with whipped cream and syrup.",
        emoji: "🥤"
    },

    // South Indian Kitchen
    {
        id: 301,
        name: "Mysore Masala Dosa",
        category: "South Indian",
        price: 8.99,
        restaurantName: "South Indian Kitchen",
        restaurantId: 3,
        description: "Crispy fermented crepe smeared with red chili chutney and stuffed with spiced potato mash.",
        emoji: "🥞"
    },
    {
        id: 302,
        name: "Steamed Ghee Idli Sambar",
        category: "South Indian",
        price: 6.99,
        restaurantName: "South Indian Kitchen",
        restaurantId: 3,
        description: "Three pillowy steamed rice cakes served with piping hot vegetable sambar and coconut chutney.",
        emoji: "🥟"
    },
    {
        id: 303,
        name: "Crispy Medu Vada",
        category: "South Indian",
        price: 5.99,
        restaurantName: "South Indian Kitchen",
        restaurantId: 3,
        description: "Golden crispy lentil donuts seasoned with peppercorns, curry leaves, and ginger.",
        emoji: "🍩"
    },
    {
        id: 304,
        name: "Traditional Filter Coffee",
        category: "Beverages",
        price: 3.49,
        restaurantName: "South Indian Kitchen",
        restaurantId: 3,
        description: "Freshly brewed aromatic chicory coffee poured with frothy whole milk in a brass tumbler.",
        emoji: "☕"
    },

    // Chinese Wok
    {
        id: 401,
        name: "Veg Hakka Noodles",
        category: "Chinese",
        price: 10.99,
        restaurantName: "Chinese Wok",
        restaurantId: 4,
        description: "Flame-tossed noodles with shredded bell peppers, cabbage, spring onion, and savory soy sauce.",
        emoji: "🍜"
    },
    {
        id: 402,
        name: "Veg Manchurian Bowl",
        category: "Chinese",
        price: 11.49,
        restaurantName: "Chinese Wok",
        restaurantId: 4,
        description: "Crispy vegetable dumplings simmered in a dark, tangy, ginger-garlic soy gravy.",
        emoji: "🍲"
    },
    {
        id: 403,
        name: "Kung Pao Chicken Bowl",
        category: "Chinese",
        price: 13.99,
        restaurantName: "Chinese Wok",
        restaurantId: 4,
        description: "Tender diced chicken wok-seared with roasted peanuts, dried chilies, and scallions.",
        emoji: "🍗"
    },
    {
        id: 404,
        name: "Crispy Golden Spring Rolls",
        category: "Chinese",
        price: 6.49,
        restaurantName: "Chinese Wok",
        restaurantId: 4,
        description: "Handmade crispy rolls packed with seasoned garden veggies, served with sweet chili dip.",
        emoji: "🥢"
    },

    // Healthy Bites
    {
        id: 501,
        name: "Mediterranean Quinoa Bowl",
        category: "Healthy",
        price: 11.99,
        restaurantName: "Healthy Bites",
        restaurantId: 5,
        description: "Organic tri-color quinoa with kalamata olives, cucumber, cherry tomatoes, and Greek feta cheese.",
        emoji: "🥗"
    },
    {
        id: 502,
        name: "Avocado Brown Rice Bowl",
        category: "Healthy",
        price: 12.49,
        restaurantName: "Healthy Bites",
        restaurantId: 5,
        description: "Fresh sliced Hass avocado, steamed edamame, roasted sweet potatoes, and tahini drizzle.",
        emoji: "🥑"
    },
    {
        id: 503,
        name: "Berry Crunch Greek Yogurt",
        category: "Healthy",
        price: 6.99,
        restaurantName: "Healthy Bites",
        restaurantId: 5,
        description: "Creamy Greek yogurt topped with fresh blueberries, strawberries, and toasted honey granola.",
        emoji: "🍓"
    },
    {
        id: 504,
        name: "Green Glow Detox Smoothie",
        category: "Beverages",
        price: 5.49,
        restaurantName: "Healthy Bites",
        restaurantId: 5,
        description: "Refreshing blend of baby spinach, green apple, fresh cucumber, ginger, and lemon zest.",
        emoji: "🥬"
    }
];

// ===================================================================
// APPLICATION STATE
// ===================================================================

let state = {
    restaurants: DEFAULT_RESTAURANTS,
    foods: DEFAULT_FOODS,
    cart: {}, // foodId -> { item: FoodItem, quantity: number }
    activeCategory: 'All',
    selectedRestaurantId: null,
    searchQuery: '',
    orders: []
};

// ===================================================================
// INITIALIZATION
// ===================================================================

document.addEventListener('DOMContentLoaded', () => {
    fetchBackendData();
    renderRestaurants();
    renderFoodMenu();
    updateCartUI();
    renderOrderHistory();
});

/**
 * Tries to fetch data from the Java backend API, falls back cleanly to local defaults.
 */
async function fetchBackendData() {
    try {
        const resp = await fetch('/api/restaurants');
        if (resp.ok) {
            const data = await resp.json();
            if (Array.isArray(data) && data.length > 0) {
                state.restaurants = data.map((r, index) => {
                    const fallback = DEFAULT_RESTAURANTS.find(dr => dr.name === r.name) || DEFAULT_RESTAURANTS[index % DEFAULT_RESTAURANTS.length];
                    return {
                        ...r,
                        bannerClass: fallback.bannerClass,
                        emoji: fallback.emoji
                    };
                });
                renderRestaurants();
            }
        }
    } catch (e) {
        // Running offline or via file:// protocol
        console.log("Using embedded sample data for restaurants.");
    }

    try {
        const resp = await fetch('/api/foods');
        if (resp.ok) {
            const data = await resp.json();
            if (Array.isArray(data) && data.length > 0) {
                state.foods = data;
                renderFoodMenu();
            }
        }
    } catch (e) {
        console.log("Using embedded sample data for foods.");
    }

    try {
        const resp = await fetch('/api/orders');
        if (resp.ok) {
            const data = await resp.json();
            if (Array.isArray(data) && data.length > 0) {
                state.orders = data.map(o => ({
                    orderId: o.orderId,
                    customerName: o.customerName,
                    customerPhone: o.customerPhone,
                    deliveryAddress: o.deliveryAddress,
                    paymentMethod: o.paymentMethod,
                    items: (o.items || []).map(i => ({
                        id: i.foodItem ? i.foodItem.id : (i.id || 0),
                        name: i.foodItem ? i.foodItem.name : (i.name || 'Food Item'),
                        price: i.unitPrice || (i.foodItem ? i.foodItem.price : 0),
                        quantity: i.quantity,
                        total: i.itemTotal || ((i.unitPrice || (i.foodItem ? i.foodItem.price : 0)) * i.quantity) || 0
                    })),
                    subtotal: o.subtotal,
                    deliveryFee: o.deliveryFee,
                    tax: o.tax,
                    grandTotal: o.totalAmount,
                    orderTime: o.orderTime,
                    status: o.orderStatus
                }));
                renderOrderHistory();
            }
        }
    } catch (e) {
        console.log("No orders on backend or running offline.");
    }
}

// ===================================================================
// RESTAURANT RENDERING
// ===================================================================

function renderRestaurants() {
    const container = document.getElementById('restaurants-container');
    if (!container) return;

    container.innerHTML = state.restaurants.map(r => `
        <div class="restaurant-card">
            <div class="restaurant-banner ${r.bannerClass || 'banner-pizza'}">
                <span>${r.emoji || '🍽️'}</span>
                <div class="restaurant-rating-badge">
                    <span class="star">★</span>
                    <span>${r.rating ? r.rating.toFixed(1) : '4.8'}</span>
                </div>
            </div>
            <div class="restaurant-info">
                <h3 class="restaurant-name">${r.name}</h3>
                <div class="restaurant-cuisine">${r.cuisine}</div>
                <div class="restaurant-meta">
                    <span class="restaurant-time">🕒 ${r.deliveryTime}</span>
                    <button class="btn btn-outline restaurant-btn" onclick="filterByRestaurant(${r.id}, '${r.name}')">
                        View Menu
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

// ===================================================================
// FOOD MENU RENDERING & FILTERING
// ===================================================================

function renderFoodMenu() {
    const container = document.getElementById('foods-container');
    if (!container) return;

    let filtered = state.foods;

    // Filter by Restaurant if selected
    if (state.selectedRestaurantId) {
        const selectedRest = state.restaurants.find(r => r.id === state.selectedRestaurantId);
        if (selectedRest) {
            filtered = filtered.filter(f => f.restaurantName.toLowerCase() === selectedRest.name.toLowerCase());
        }
    }

    // Filter by Category
    if (state.activeCategory && state.activeCategory !== 'All') {
        filtered = filtered.filter(f => f.category.toLowerCase() === state.activeCategory.toLowerCase());
    }

    // Filter by Search Query
    if (state.searchQuery && state.searchQuery.trim() !== '') {
        const query = state.searchQuery.toLowerCase().trim();
        filtered = filtered.filter(f => 
            f.name.toLowerCase().includes(query) ||
            f.category.toLowerCase().includes(query) ||
            f.description.toLowerCase().includes(query) ||
            f.restaurantName.toLowerCase().includes(query)
        );
    }

    if (filtered.length === 0) {
        container.innerHTML = `
            <div style="grid-column: 1 / -1; text-align: center; padding: 48px; background: #fff; border-radius: 16px; border: 1px solid var(--border);">
                <div style="font-size: 3rem; margin-bottom: 12px;">🔍</div>
                <h3>No food items found</h3>
                <p style="color: var(--text-secondary); margin-top: 6px;">Try adjusting your search or category filter.</p>
                <button class="btn btn-primary btn-sm" style="margin-top: 16px;" onclick="resetAllFilters()">Reset Filters</button>
            </div>
        `;
        return;
    }

    container.innerHTML = filtered.map(item => {
        const inCart = state.cart[item.id];
        const qty = inCart ? inCart.quantity : 0;

        return `
            <div class="food-card">
                <div class="food-image-box">
                    <span>${item.emoji || '🍽️'}</span>
                    <span class="food-category-tag">${item.category}</span>
                    <span class="food-restaurant-tag">${item.restaurantName}</span>
                </div>
                <div class="food-details">
                    <h3 class="food-title">${item.name}</h3>
                    <p class="food-description">${item.description}</p>
                    <div class="food-card-footer">
                        <span class="food-price">$${item.price.toFixed(2)}</span>
                        ${qty > 0 ? `
                            <div class="qty-control-inline">
                                <button class="qty-btn" onclick="updateItemQuantity(${item.id}, ${qty - 1})">-</button>
                                <span class="qty-value">${qty}</span>
                                <button class="qty-btn" onclick="updateItemQuantity(${item.id}, ${qty + 1})">+</button>
                            </div>
                        ` : `
                            <button class="btn-add-cart" onclick="addItemToCart(${item.id})">
                                <span>Add to Cart</span> <span>+</span>
                            </button>
                        `}
                    </div>
                </div>
            </div>
        `;
    }).join('');
}

function filterByCategory(category) {
    state.activeCategory = category;

    // Update active pill UI
    const pills = document.querySelectorAll('.category-pills .pill');
    pills.forEach(pill => {
        if (pill.textContent.includes(category)) {
            pill.classList.add('active');
        } else {
            pill.classList.remove('active');
        }
    });

    renderFoodMenu();
}

function filterByRestaurant(restaurantId, restaurantName) {
    state.selectedRestaurantId = restaurantId;
    const infoText = document.getElementById('menu-current-filter-info');

    if (restaurantId && restaurantName) {
        if (infoText) {
            infoText.innerHTML = `Showing menu for <strong>${restaurantName}</strong> <button onclick="filterByRestaurant(null)" style="background:none; border:none; color:var(--primary); font-weight:700; cursor:pointer; text-decoration:underline; margin-left:8px;">(Show All)</button>`;
        }
        // Smooth scroll to menu section
        const menuElem = document.getElementById('menu');
        if (menuElem) {
            menuElem.scrollIntoView({ behavior: 'smooth' });
        }
    } else {
        if (infoText) {
            infoText.textContent = "Browse all mouth-watering items prepared on order";
        }
    }

    renderFoodMenu();
}

function handleSearch(query) {
    state.searchQuery = query;

    // Sync search inputs
    const navSearch = document.getElementById('nav-search-input');
    const menuSearch = document.getElementById('menu-search-input');
    if (navSearch && navSearch.value !== query) navSearch.value = query;
    if (menuSearch && menuSearch.value !== query) menuSearch.value = query;

    renderFoodMenu();
}

function resetAllFilters() {
    state.activeCategory = 'All';
    state.selectedRestaurantId = null;
    state.searchQuery = '';
    const navSearch = document.getElementById('nav-search-input');
    const menuSearch = document.getElementById('menu-search-input');
    if (navSearch) navSearch.value = '';
    if (menuSearch) menuSearch.value = '';
    filterByCategory('All');
    filterByRestaurant(null);
}

// ===================================================================
// CART LOGIC
// ===================================================================

function addItemToCart(foodId) {
    const item = state.foods.find(f => f.id === foodId);
    if (!item) return;

    if (state.cart[foodId]) {
        state.cart[foodId].quantity += 1;
    } else {
        state.cart[foodId] = { item, quantity: 1 };
    }

    showToast(`Added 1x ${item.name} to cart`);
    updateCartUI();
    renderFoodMenu();
}

function updateItemQuantity(foodId, newQty) {
    if (!state.cart[foodId]) return;

    const itemName = state.cart[foodId].item.name;

    if (newQty <= 0) {
        delete state.cart[foodId];
        showToast(`Removed ${itemName} from cart`);
    } else {
        state.cart[foodId].quantity = newQty;
    }

    updateCartUI();
    renderFoodMenu();
}

function removeItemFromCart(foodId) {
    if (state.cart[foodId]) {
        const itemName = state.cart[foodId].item.name;
        delete state.cart[foodId];
        showToast(`Removed ${itemName} from cart`);
        updateCartUI();
        renderFoodMenu();
    }
}

function clearActiveCart() {
    state.cart = {};
    showToast("Your cart has been cleared");
    updateCartUI();
    renderFoodMenu();
}

function calculateCartTotals() {
    let subtotal = 0;
    let itemCount = 0;

    for (const id in state.cart) {
        const entry = state.cart[id];
        subtotal += entry.item.price * entry.quantity;
        itemCount += entry.quantity;
    }

    const deliveryFee = (subtotal === 0 || subtotal >= 30.0) ? 0.0 : 2.99;
    const tax = Math.round((subtotal * 0.08) * 100) / 100;
    const grandTotal = Math.round((subtotal + deliveryFee + tax) * 100) / 100;

    return {
        itemCount,
        subtotal: Math.round(subtotal * 100) / 100,
        deliveryFee,
        tax,
        grandTotal
    };
}

function updateCartUI() {
    const totals = calculateCartTotals();

    // Badge in navbar
    const badge = document.getElementById('cart-badge-count');
    if (badge) badge.textContent = totals.itemCount;

    // Header in cart drawer
    const headerCount = document.getElementById('cart-header-count');
    if (headerCount) headerCount.textContent = `${totals.itemCount} item${totals.itemCount === 1 ? '' : 's'}`;

    // Cart items container
    const cartContainer = document.getElementById('cart-items-container');
    const cartFooter = document.getElementById('cart-footer');

    if (!cartContainer) return;

    if (totals.itemCount === 0) {
        cartContainer.innerHTML = `
            <div class="empty-cart-view">
                <div class="empty-cart-icon">🛒</div>
                <h4>Your cart is empty</h4>
                <p>Looks like you haven't added anything yet. Choose delicious food from our menu!</p>
                <button class="btn btn-primary btn-sm" style="margin-top: 16px;" onclick="closeCartModal()">
                    Browse Menu
                </button>
            </div>
        `;
        if (cartFooter) cartFooter.style.display = 'none';
        return;
    }

    if (cartFooter) cartFooter.style.display = 'block';

    const cartHtml = Object.values(state.cart).map(({ item, quantity }) => `
        <div class="cart-item-card">
            <div class="cart-item-info">
                <span class="cart-item-emoji">${item.emoji || '🍽️'}</span>
                <div>
                    <div class="cart-item-title">${item.name}</div>
                    <div class="cart-item-sub">${item.restaurantName} • $${item.price.toFixed(2)}</div>
                </div>
            </div>
            <div class="cart-item-actions">
                <div class="qty-control-inline">
                    <button class="qty-btn" onclick="updateItemQuantity(${item.id}, ${quantity - 1})">-</button>
                    <span class="qty-value">${quantity}</span>
                    <button class="qty-btn" onclick="updateItemQuantity(${item.id}, ${quantity + 1})">+</button>
                </div>
                <span class="cart-item-price">$${(item.price * quantity).toFixed(2)}</span>
                <button class="cart-item-remove-btn" title="Remove item" onclick="removeItemFromCart(${item.id})">🗑️</button>
            </div>
        </div>
    `).join('');

    cartContainer.innerHTML = cartHtml;

    // Price breakdowns
    document.getElementById('cart-subtotal').textContent = `$${totals.subtotal.toFixed(2)}`;
    document.getElementById('cart-delivery-fee').textContent = totals.deliveryFee === 0 ? 'FREE' : `$${totals.deliveryFee.toFixed(2)}`;
    document.getElementById('cart-tax').textContent = `$${totals.tax.toFixed(2)}`;
    document.getElementById('cart-grand-total').textContent = `$${totals.grandTotal.toFixed(2)}`;
}

// ===================================================================
// MODAL CONTROLS
// ===================================================================

function openCartModal() {
    const backdrop = document.getElementById('cart-modal-backdrop');
    if (backdrop) backdrop.classList.add('active');
}

function closeCartModal(event) {
    if (event && event.target && event.target.id !== 'cart-modal-backdrop' && !event.target.classList.contains('close-btn')) {
        return;
    }
    const backdrop = document.getElementById('cart-modal-backdrop');
    if (backdrop) backdrop.classList.remove('active');
}

function openCheckoutModal() {
    const totals = calculateCartTotals();
    if (totals.itemCount === 0) {
        showToast("Add items to your cart before proceeding to checkout.");
        return;
    }

    closeCartModal();

    document.getElementById('checkout-items-count').textContent = `${totals.itemCount} item${totals.itemCount === 1 ? '' : 's'}`;
    document.getElementById('checkout-total-payable').textContent = `$${totals.grandTotal.toFixed(2)}`;

    const checkoutModal = document.getElementById('checkout-modal-backdrop');
    if (checkoutModal) checkoutModal.classList.add('active');
}

function closeCheckoutModal(event) {
    if (event && event.target && event.target.id !== 'checkout-modal-backdrop' && !event.target.classList.contains('close-btn') && !event.target.classList.contains('btn-secondary')) {
        return;
    }
    const checkoutModal = document.getElementById('checkout-modal-backdrop');
    if (checkoutModal) checkoutModal.classList.remove('active');
}

function closeConfirmationModal() {
    const confirmationModal = document.getElementById('confirmation-modal-backdrop');
    if (confirmationModal) confirmationModal.classList.remove('active');
}

// ===================================================================
// ORDER PLACEMENT FLOW
// ===================================================================

async function handlePlaceOrder(event) {
    event.preventDefault();

    const name = document.getElementById('cust-name').value.trim();
    const phone = document.getElementById('cust-phone').value.trim();
    const address = document.getElementById('cust-address').value.trim();
    const paymentRadio = document.querySelector('input[name="payment"]:checked');
    const payment = paymentRadio ? paymentRadio.value : 'Cash on Delivery';

    if (!name || !phone || !address) {
        showToast("Please fill in all required fields.");
        return;
    }

    const totals = calculateCartTotals();
    const orderedItems = Object.values(state.cart).map(entry => ({
        id: entry.item.id,
        name: entry.item.name,
        price: entry.item.price,
        quantity: entry.quantity,
        restaurantName: entry.item.restaurantName,
        total: entry.item.price * entry.quantity
    }));

    // Generate Order ID
    const randomId = Math.floor(1000 + Math.random() * 9000);
    let orderId = `QB-${randomId}`;

    // Try posting to Java backend if available
    try {
        const payload = {
            customerName: name,
            customerPhone: phone,
            deliveryAddress: address,
            paymentMethod: payment,
            items: orderedItems.map(i => ({ id: i.id, quantity: i.quantity }))
        };

        const resp = await fetch('/api/orders', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (resp.ok) {
            const result = await resp.json();
            if (result.orderId) {
                orderId = result.orderId;
            }
        }
    } catch (e) {
        console.log("Backend offline or local file mode. Order recorded locally.");
    }

    const newOrder = {
        orderId,
        customerName: name,
        customerPhone: phone,
        deliveryAddress: address,
        paymentMethod: payment,
        items: orderedItems,
        subtotal: totals.subtotal,
        deliveryFee: totals.deliveryFee,
        tax: totals.tax,
        grandTotal: totals.grandTotal,
        orderTime: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', month: 'short', day: 'numeric' }),
        status: "CONFIRMED"
    };

    // Store in session order history
    state.orders.unshift(newOrder);

    // Clear cart and checkout
    state.cart = {};
    updateCartUI();
    renderFoodMenu();
    closeCheckoutModal();

    // Reset form
    document.getElementById('checkout-form').reset();

    // Display confirmation receipt
    displayOrderConfirmation(newOrder);
    renderOrderHistory();
    showToast(`Order #${orderId} placed successfully!`);
}

function displayOrderConfirmation(order) {
    const receiptCard = document.getElementById('receipt-card');
    if (!receiptCard) return;

    receiptCard.innerHTML = `
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px;">
            <span class="receipt-id-badge">Order #${order.orderId}</span>
            <span style="color: var(--text-muted); font-size: 0.82rem;">${order.orderTime}</span>
        </div>

        <div class="receipt-row">
            <span>Customer:</span>
            <strong>${order.customerName} (${order.customerPhone})</strong>
        </div>
        <div class="receipt-row">
            <span>Deliver to:</span>
            <strong>${order.deliveryAddress}</strong>
        </div>
        <div class="receipt-row">
            <span>Payment Method:</span>
            <strong>${order.paymentMethod}</strong>
        </div>

        <div class="receipt-divider"></div>

        <div style="margin-bottom: 8px; font-weight: 700; color: var(--text-primary);">Order Items:</div>
        ${order.items.map(i => `
            <div class="receipt-row" style="font-size: 0.85rem;">
                <span>${i.name} × ${i.quantity}</span>
                <span>$${i.total.toFixed(2)}</span>
            </div>
        `).join('')}

        <div class="receipt-divider"></div>

        <div class="receipt-row">
            <span>Subtotal:</span>
            <span>$${order.subtotal.toFixed(2)}</span>
        </div>
        <div class="receipt-row">
            <span>Delivery Fee:</span>
            <span>${order.deliveryFee === 0 ? 'FREE' : `$${order.deliveryFee.toFixed(2)}`}</span>
        </div>
        <div class="receipt-row">
            <span>Taxes & Fees (8%):</span>
            <span>$${order.tax.toFixed(2)}</span>
        </div>
        <div class="receipt-divider"></div>
        <div class="receipt-row" style="font-size: 1.1rem; font-weight: 800; color: var(--primary);">
            <span>Total Paid:</span>
            <span>$${order.grandTotal.toFixed(2)}</span>
        </div>
    `;

    const confirmationModal = document.getElementById('confirmation-modal-backdrop');
    if (confirmationModal) confirmationModal.classList.add('active');
}

// ===================================================================
// ORDER HISTORY DISPLAY
// ===================================================================

function renderOrderHistory() {
    const container = document.getElementById('orders-list-container');
    if (!container) return;

    if (state.orders.length === 0) {
        container.innerHTML = `
            <div style="text-align: center; padding: 48px; background: #ffffff; border-radius: 16px; border: 1px solid var(--border);">
                <div style="font-size: 3rem; margin-bottom: 12px;">📦</div>
                <h3>No orders placed yet</h3>
                <p style="color: var(--text-secondary); margin-top: 6px;">Once you place an order, track it right here in real time!</p>
                <a href="#menu" class="btn btn-primary btn-sm" style="margin-top: 16px;">Order Now</a>
            </div>
        `;
        return;
    }

    container.innerHTML = state.orders.map(order => `
        <div class="order-history-card">
            <div class="order-card-header">
                <div>
                    <span class="order-card-id">Order #${order.orderId}</span>
                    <span style="margin-left: 8px; color: var(--text-muted); font-size: 0.85rem;">• ${order.orderTime}</span>
                </div>
                <span class="order-status-badge">✅ ${order.status}</span>
            </div>

            <div class="order-card-items">
                <strong>Items: </strong>
                ${order.items.map(i => `${i.name} (x${i.quantity})`).join(', ')}
            </div>

            <div class="order-card-footer">
                <div>Deliver to: <strong>${order.deliveryAddress}</strong></div>
                <div class="order-card-total">$${order.grandTotal.toFixed(2)}</div>
            </div>
        </div>
    `).join('');
}

function viewOrdersHistoryTab() {
    closeConfirmationModal();
    const ordersSection = document.getElementById('orders-history');
    if (ordersSection) {
        ordersSection.scrollIntoView({ behavior: 'smooth' });
    }
}

// ===================================================================
// TOAST NOTIFICATIONS
// ===================================================================

function showToast(message) {
    const container = document.getElementById('toast-container');
    if (!container) return;

    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.innerHTML = `<span>✨</span><span>${message}</span>`;

    container.appendChild(toast);

    setTimeout(() => {
        if (toast && toast.parentNode) {
            toast.parentNode.removeChild(toast);
        }
    }, 3000);
}
