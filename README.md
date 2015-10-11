JsonQL Filter
======

JsonQL is a Java implementation of JsonQL format

### json_filter
```
	["==", key, value] equality: key = value
	["~~", key, value] equality: key LIKE value
	["!=", key, value] inequality: key ≠ value
	[">", key, value] greater than: key > value
	[">=", key, value] greater than or equal: key ≥ value
	["<", key, value] less than: key < value
	["<=", key, value] less than or equal: key ≤ value
	["in", key, v0, ..., vn] set inclusion: key ∈ {v0, ..., vn}
	["!in", key, v0, ..., vn] set exclusion: key ∉ {v0, ..., vn}
	["between", key, v1, v2] key between v1 and v2
	["null", key] key is null
	["!null", key] key is not null
	["all", f0, ..., fn] logical AND: f0 ∧ ... ∧ fn
	["any", f0, ..., fn] logical OR: f0 ∨ ... ∨ fn
	["none", f0, ..., fn] logical NOR: ¬f0 ∧ ... ∧ ¬fn
	[@functionName, v1, ... vn] logical function expression
```
example:

```json
	[
	  "all",
	  ["==", "class", "street_limited"],
	  [">=", "admin_level", 3],
	  ["!in", "category.id", 1, 2, 4],
	  ["@within", "$geom", ["@bbox", 44,55,50,60]]
	]
```

### json_select

```json

	[key, keyn, [functionName, v0, ... , vn], [keyn, astitle]]

	example:

	["id", "name", "category.id", "category.name", [["@concat", "$title", " ", "$width"], "title"]]
```
### json_order

```json
	[direction, key, ..., keyn]
	||
	[[direction,key,...keyn], ... , [direction, keyn]]

	example:

	[["asc", "name"], ["desc","id"]]
```

### Usage

```java
    FilterRequestRequestParser parser = FilterRequestRequestParser.getInstance();
    Filter filter = parser.parse("[\"==\", \"class\", \"street_limited\"]");
    
```

