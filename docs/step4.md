## 5. Special Sorting, Filters and Facets

In this step we show which kind of special filters, facets and sorting the searchlib provides.

### 5.1 Special Sorting

```java
//special sort filter allows to combine a date with scoring, so
//that best fitting and latest documents are ranked to top
search.sort(desc(scoredDate(created)));

//special sorting which gives results scored by distance to a 
//given location. The distance is meassured based on the 
//geoDistance defined for the search. 
search.sort(desc(distance()));
```

**Future Extensions:** Support more sortings

### 5.2 Type-aware filters

For numeric and date fields, special filters are supported.

```java
//between filter for datetime fields
search.filter(created.between(
                ZonedDateTime.now().minusDays(7),
                ZonedDateTime.now()
        )
);

//greater than filter for numeric fields
search.filter(ranking.greaterThan(3));
```

This is list of the type specific filters currently supported by the field descriptor syntax:

| Numeric  | Date | Text | Location | All |
|---|---|---|---|---|
| between  | between | equals | withinBBox | isEmpty |
| greaterThan | after | prefix | withinCircle | isNotEmpty |
| lesserThan | before |  |  | |

**Future Extensions:** Support more special filters 

### 5.3 Special Field Facets

The library support several kind of facets. Facets have names, so they can be referenced in the result.
Do not use the same name for more than one facet (they will be overwritten). For names only alphanumeric chars are supported.

```java
//lets start with the range facet. It needs start, end and gap and is type aware.
search.facet(range("dates", created, ZonedDateTime.now().minus(Duration.ofDays(100)), ZonedDateTime.now(), Duration.ofDays(10)));

//query facets support the filters we already know from the queries, simple ones and complex
search.facet(query("middle", eq(category, 5L)));

search.facet(query("hotAndNew", and(category.between(7,10), created.after(ZonedDateTime.now().minus(Duration.ofDays(1))))))
     
//stats facet support statistics for facet field, like max, min, etc.
//it can be defined which stats should be returned, in this case count, sum and percentile
search.facet(stats("catStats", category).count().sum().percentiles(1,99,99.9));

//interval facets allows to perform faceting on a defined group of dates or numeric periodes; for each of those intervals it is needed to 
//provide a name the start value, the end value, if it is open (includes the value) or closed (does not include the value) on the start
//and on the end. If not specified both sides are open.
search.facet(
        interval("quality", marker,
                Interval.numericInterval("low", 0L, 2L, true, false),
                Interval.numericInterval("high", 3L, 4L)
        )
);

//pivot facets allows facetting on facets
search.facet(pivot("catsCreated", category, created));             
```

It is also supported by the library the combination of pivots with the other kind of facets.

**This is a work in progress**

```java
search.facet(pivot("catsNew", category, query("new",category.between(7,10)))); 

//if you want to use facets on the same level you can use lists
search.facet(pivot("catsNew", list(category, query("new",category.between(7,10))))); 
```
