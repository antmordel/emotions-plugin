# e-Motions plug-in 
*A graphical framework for the specification, simulation and analysis of Real-Time sytems.*

- *Status*: Developer version 2.1.0 (alpha)

An extensive tutorial and information with a vast range of examples can be found in the [Atenea group's website](http://atenea.lcc.uma.es/index.php/Main_Page/Resources/E-motions).

### How to report errors?
Please, note that this is an alpha version. Report issues on this project by email to [amoreno@lcc.uma.es](mailto:amoreno@lcc.uma.es). Please, add enough information to reproduce the problem.

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
- Added full support for Palladio specifications. We provided [an explicit behavior for the Palladio Component Model][1]. In that paper, transformations performing the flattening of the input Palladio models had to be independently executed. In the current version of the tool, we provide a new button ![palladio button](https://raw.githubusercontent.com/e-motions/e-motions_plugin/master/e-motions_plugin/icons/palladio.png) that triggers its execution automatically.
- Since fulfilling the *Palladio in e-Motions* dialog is quite cumbersome, the last launch is saved so that it can be re-used. The project containing the file with the rules should be selected before triggering the *Palladio in e-Motions* dialog.

[1]: http://link.springer.com/chapter/10.1007%2F978-3-319-09195-2_9





