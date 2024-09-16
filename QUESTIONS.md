# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```txt
I would choose one way to implement a database access layer and try stick to it in the project.
That way a new contributor from the team could see the standard way, and not think everytime what to choose.

The way it's implemented for `Product` and `Store` entity in this project could bring problems in future. 
The code exposes its internal entities (database structure), which means that it will be much harder to
sustain contract with outside world while changing something in the database.
I would stick to the concept that the database structure should be internal, and for outside connections
different classes should be exposed.

The warehouse access layer looks like hexagonal architecture. The drawbacks that it has more boilerplate, takes more memory because of the amount 
of instances it generates for any request. But it does clearly separate the concepts of DB access layer 
and domain, which could be helpful when the project becomes bigger. It makes the packages loosely coupled.

I'd probably change the product and store data-access layer so it at least has different types for API.  
```


----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```txt
I prefer to keep one source of truth for the API. It could be either code (from which a spec is generated) or an open-api spec.
From my previous experiece when it comes to intergration between teams or services, it's usually
a good idea to come with a contract (open-api spec) first, and then implement it.
In that way both a server and a client could be generated from it. Like in the warehouse package.

The way it's implemented for products/stores - it's faster to do like this, good for prototyping.
Also could be acceptable if it's only used by team's internal services if there is no time for documenting it.

In most cases my choice would be creating a spec and then generating the code from it.
```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
```txt
I don't like the idea of dropping tests. For this type of project I'd prioritize such tests:
- Domain validation - checking that every use case is working according to business requirements.
- tests that validate the api contract. They are very important, since some services/teams could rely on the contract. 
So it's important to be sure that it's working.

That should give a good coverage.

To keep the quality of the project and the code coverage I'd add a tool (like sonar) 
in the build (or the pipeline). It could be set up to give errors/warnings in case
the quality is below some threshold. 
```