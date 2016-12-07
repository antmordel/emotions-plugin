# e-Motions plug-in 
*A graphical framework for the specification, simulation and analysis of Real-Time sytems.*

- *Status*: Developer version 2.3.0 (alpha)

An extensive tutorial and information with a vast range of examples can be found in the [Atenea group's website](http://atenea.lcc.uma.es/index.php/Main_Page/Resources/E-motions).

### How to report errors?
Please, note that this is an alpha version. Report issues on this project by email to [amoreno@lcc.uma.es](mailto:amoreno@lcc.uma.es). Please, add enough information to reproduce the problem.

# How to install e-Motions?
In any Eclipse for Modelling (Eclipse Modelling Tools):

1. Download a Eclipse Modelling as the following
    https://www.eclipse.org/downloads/packages/eclipse-modeling-tools/neon1a

2. Install ATL
    http://download.eclipse.org/mmt/atl/updates/releases/3.7/

3. Install e-Motions (Java 1.8 needed!)
    http://lcc.uma.es/~amoreno/e-motions/
    
After the installation, the two icons (![emotions button](https://raw.githubusercontent.com/e-motions/e-motions_plugin/master/e-motions_plugin/icons/cog.png)![palladio button](https://raw.githubusercontent.com/e-motions/e-motions_plugin/master/e-motions_plugin/icons/palladio.png)) should have been appeared.
    
# Getting the gist
## e-Motions
To get a flavour of what e-Motions is, you can download some example projects on [this repository](https://github.com/e-motions/emotions_projects). 
In this repository you can find the [Production Line System](http://atenea.lcc.uma.es/index.php/Main_Page/Resources/E-motions/PLSExample) in which this mini-tutorial is based on.
1. Clone the repository into your local computer.
2. Open the Eclipse with the e-Motions plug-in.
3. Import the project under the folder `/emotions_projects/pls`.
4. Click on the cog wheel ![emotions button](https://raw.githubusercontent.com/e-motions/e-motions_plugin/master/e-motions_plugin/icons/cog.png) and fill out all the text fields and check boxes.

# Changelog
### 2.3.0
- Added a file to store the last launch configuration. To read an existing launch configuration, just select the project in which e-Motions was fired and press the e-Motions button afterwords.
### 2.1.0
- Added full support for Palladio specifications. We provided [an explicit behavior for the Palladio Component Model][1]. In that paper, transformations performing the flattening of the input Palladio models had to be independently executed. In the current version of the tool, we provide a new button ![palladio button](https://raw.githubusercontent.com/e-motions/e-motions_plugin/master/e-motions_plugin/icons/palladio.png) that triggers its execution automatically.
- Since fulfilling the *Palladio in e-Motions* dialog is quite cumbersome, the last launch is saved so that it can be re-used. The project containing the file with the rules should be selected before triggering the *Palladio in e-Motions* dialog.





