package com.represa.draw.data

data class Dessert(val url: String, val name: String, val description: String, val price: String) {

}

val desserts = mutableListOf(
    Dessert("https://images.unsplash.com/photo-1582576601037-b5050b45a44c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1950&q=80",
        "Strawberry Waffle",
        "Eggs, milk, coconout oil, orange, lemon, sugar, cream, syrup (chocolate, maple)",
        "$4.50"),
    Dessert("https://images.unsplash.com/photo-1562945431-ce2b63d5a7fe?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1950&q=80",
        "Chocolate Donut",
        "Eggs, milk, palm oil, lemon, sugar, butter",
        "$2"),
    Dessert("https://images.unsplash.com/photo-1617806501441-2a4a45c5316c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1950&q=80",
        "Turkish Baklava",
        "Butter, sugar, pistachios, lemon ",
        "$0,70/p"),
    Dessert("https://images.unsplash.com/photo-1587314168485-3236d6710814?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1570&q=80",
        "Strawberry Crepes",
        "Eggs, milk, butter, strawberry, lemon, sugar, cream, syrup (chocolate, maple)",
        "$3.75")
)