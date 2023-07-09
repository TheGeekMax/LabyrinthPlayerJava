package tools

data class Vector2Int(var x: Int, var y: Int) {
    fun moduleSqred(): Int = x * x + y * y
}