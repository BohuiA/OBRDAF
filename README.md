# OBRDAF
Object Based Rational Database Application Framework

OBRDAF framework includes many useful patterns and libraries that suit on many industrial applications. OBRDAF will help developers
to focus more on logic instead of details of achievements. With patterns/libraries of OBRDAF, developers only needs to give logic
or commands about what results they want to obtains, OBRDAF will do the rest work for you, including creating detail algorithms, coding,
communicating database, logic optimization by source code, etc.

OBRDAF is still under developing with more useful and applicable patterns/libraries. The application scope of OBRDAF will be on both
industrial product with rational databases and web application with light key-value databases.

Example 1:  
When Java domain developers want to select some users with specific filters from database, without OBRDAF, they need to think about how
to use SQL language and spend time to optimize the SQL commands for meeting performance requirements. 
With the Query Object pattern or the Repository pattern of OBRDAF, the only thing they need to think about is what fields and
filter settings needed, and pass fields and filtering settings to the applied patterns, OBRDAF will create, optimize, communicate,
and produce their logic and return the expected result sets to them.

Example 2:
When Java domain developers want to store or load logic tree or diagrams from rational database to RAM or verse. Without OBRDAF,
they need to take care of storing/loading details, for example, field columns, inheritance of classes, etc.
With the Concrete Table Inheritance library or the Class Table Inheritance library of OBRDAF, developers only need to call library
interfaces and pass target root instances, OBRDAF will help developers take care of rest working about storing/loading with user defined
monitoring class.

Eventually, OBRDAF will help developers only focus on their domain areas and free them from other unfamiliar areas by many applicable
libraries or patterns.
