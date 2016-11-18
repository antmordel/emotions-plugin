# e-Motions plug-in 
*A graphical framework for the specification, simulation and analysis of Real-Time sytems.*

- *Status*: Developer version 2.1.0 (alpha)

An extensive tutorial and information with a vast range of examples can be found in the [Atenea group's website](http://atenea.lcc.uma.es/index.php/Main_Page/Resources/E-motions).

### How to report errors?
Please note that this version is an alpha version and we are doing lots of changes. Please report issues on this project and giving the steps for reproducing the error. Specific information could be sent to [amoreno@lcc.uma.es](mailto:amoreno@lcc.uma.es).

# How to install e-Motions?
In any Eclipse Modelling:

1. Install ATL
    http://download.eclipse.org/mmt/atl/updates/releases/3.6/

2. Install Xtend
    http://download.eclipse.org/modeling/tmf/xtext/updates/composite/releases/ 
    * Only XTend IDE (under Xtend category) is needed.

3. Install e-Motions 
    http://lcc.uma.es/~amoreno/e-motions/

# Changelog
### 2.1.0
- Added full support for the Palladio specification. We have presented [a explicit behavior for the Palladio Component Model][1]. However, as it was mentioned in that work, previous transformations (especifically flattening the input Palladio models) should be done. In this version we provide a new button ![palladio button](https://raw.githubusercontent.com/e-motions/e-motions_plugin/master/e-motions_plugin/icons/palladio.png) has been added giving support to this changes.
- Since fulfilling the *Palladio in e-Motions* dialog is quite cumbersome, we provide a new file that keeps the last launch of e-Motions. The project containing the file with the rules should be selected before triggering the *Palladio in e-Motions* dialog.





