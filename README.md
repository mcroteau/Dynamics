# Dynamics +Gain

user walks up to screen
user sees a dashboard of his/her city with total homeless
user click give
user selects a shelter
user sees a list of donation options, a blurb and what they need in addition to money
user clicks home button
user sees total homeless registered in application
can click per country
per state
per city
per shelter


The idea:


<bean depends-on="dataSource" class="org.springframework.beans.factory.config.MethodInvokingBean">
    <property name="targetClass" value="org.hsqldb.util.DatabaseManagerSwing" />
    <property name="targetMethod" value="main" />
    <property name="arguments">
        <list>
            <value>--url</value>
            <value>jdbc:h2:mem:ok</value>
            <value>--user</value>
            <value>sa</value>
            <value>--password</value>
            <value></value>
        </list>
    </property>
</bean>