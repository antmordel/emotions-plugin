/**
 * @author Antonio Moreno-Delgado <i>amoreno@lcc.uma.es</i>
 * @date May 4th 2014
 * 
 * 
 *  This file is part of e-Motions. It has been generated with Xtend.
 *
 *  e-Motions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  e-Motions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with e-Motions.  If not, see <http://www.gnu.org/licenses/>.
 *  
 */
package es.uma.lcc.e_motions.codegeneration;

import Maude.*;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class Model2TextMaude {
  private final boolean PRETTY_PRINT = true;
  
  private final int PRETTY_LINE_LIMIT = 150;
  
  public Object MaudeM2T() {
    return null;
  }
  
  public static void generate(final String model, final String output) {
    Model2TextMaude _emotionsMaudeM2T = new Model2TextMaude();
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(output);
    _emotionsMaudeM2T.generate(model, _newArrayList);
  }
  
  public void generate(final String model, final List<String> output) {
    try {
      this.doEMFSetup();
      final ResourceSetImpl resourceSet = new ResourceSetImpl();
      EPackage.Registry _packageRegistry = resourceSet.getPackageRegistry();
      _packageRegistry.put(MaudePackage.eNS_URI, MaudePackage.eINSTANCE);
      URI _createURI = URI.createURI(model);
      final Resource resource = resourceSet.getResource(_createURI, true);
      final int cont = 0;
      EList<EObject> _contents = resource.getContents();
      Iterable<MaudeSpec> _filter = Iterables.<MaudeSpec>filter(_contents, MaudeSpec.class);
      for (final MaudeSpec maudespec : _filter) {
        {
          String _get = output.get(cont);
          final FileWriter fw = new FileWriter(_get);
          final BufferedWriter bw = new BufferedWriter(fw);
          CharSequence _generateCode = this.generateCode(maudespec);
          String _string = _generateCode.toString();
          bw.write(_string);
          bw.close();
          fw.close();
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public String prettyPrint(final String string) {
    String res = string;
    boolean changes = true;
    if (this.PRETTY_PRINT) {
      boolean _while = changes;
      while (_while) {
        {
          changes = false;
          String[] _split = res.split("\n");
          for (final String l : _split) {
            int _length = l.length();
            boolean _greaterThan = (_length > this.PRETTY_LINE_LIMIT);
            if (_greaterThan) {
              changes = true;
              int _indexOf = l.indexOf(">");
              boolean _lessEqualsThan = (_indexOf <= this.PRETTY_LINE_LIMIT);
              if (_lessEqualsThan) {
                int _indexOf_1 = l.indexOf(">");
                int _plus = (_indexOf_1 + 1);
                String _substring = l.substring(0, _plus);
                String _plus_1 = (_substring + "\n");
                int _indexOf_2 = l.indexOf("<");
                String _generateSpaces = this.generateSpaces(_indexOf_2);
                String _plus_2 = (_plus_1 + _generateSpaces);
                int _indexOf_3 = l.indexOf(">");
                int _plus_3 = (_indexOf_3 + 1);
                String _substring_1 = l.substring(_plus_3);
                String _plus_4 = (_plus_2 + _substring_1);
                String _replace = res.replace(l, _plus_4);
                res = _replace;
              } else {
                int _indexOf_4 = l.indexOf(" ", 100);
                String _substring_2 = l.substring(0, _indexOf_4);
                String _plus_5 = (_substring_2 + "\n");
                int _indexOf_5 = l.indexOf(" ", 100);
                String _generateSpaces_1 = this.generateSpaces(_indexOf_5);
                String _plus_6 = (_plus_5 + _generateSpaces_1);
                int _indexOf_6 = l.indexOf(" ", 100);
                int _plus_7 = (_indexOf_6 + 1);
                String _substring_3 = l.substring(_plus_7);
                String _plus_8 = (_plus_6 + _substring_3);
                String _replace_1 = res.replace(l, _plus_8);
                res = _replace_1;
              }
            }
          }
        }
        _while = changes;
      }
      String[] _split = string.split("\n");
      for (final String l : _split) {
        int _indexOf = l.indexOf("rl");
        boolean _startsWith = l.startsWith("rl", _indexOf);
        if (_startsWith) {
          String _replace = res.replace(l, ("\n" + l));
          res = _replace;
        }
      }
    }
    return res;
  }
  
  public String generateSpaces(final int i) {
    String res = "";
    if ((i < 30)) {
      IntegerRange _upTo = new IntegerRange(0, (i - 2));
      for (final Integer j : _upTo) {
        res = (res + " ");
      }
    } else {
      res = "          ";
    }
    return res;
  }
  
  public String firstSpaces(final String string) {
    StringConcatenation _builder = new StringConcatenation();
    String res = _builder.toString();
    String[] _split = string.split("(?!^)");
    for (final String ch : _split) {
      boolean _equals = ch.equals(" ");
      if (_equals) {
        InputOutput.<String>println("entra");
        res = (res + " ");
      }
    }
    return res;
  }
  
  public Object doEMFSetup() {
    Map<String, Object> _extensionToFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
    XMIResourceFactoryImpl _xMIResourceFactoryImpl = new XMIResourceFactoryImpl();
    return _extensionToFactoryMap.put(
      "xmi", _xMIResourceFactoryImpl);
  }
  
  public CharSequence generateCode(final MaudeSpec spec) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _header = this.header();
    _builder.append(_header, "");
    _builder.newLineIfNotEmpty();
    {
      EList<MaudeTopEl> _els = spec.getEls();
      Iterable<SModule> _filter = Iterables.<SModule>filter(_els, SModule.class);
      for(final SModule mod : _filter) {
        {
          boolean _and = false;
          boolean _and_1 = false;
          boolean _and_2 = false;
          boolean _and_3 = false;
          String _name = mod.getName();
          boolean _notEquals = (!Objects.equal(_name, "@ECORE@"));
          if (!_notEquals) {
            _and_3 = false;
          } else {
            String _name_1 = mod.getName();
            boolean _notEquals_1 = (!Objects.equal(_name_1, "E-MOTIONS"));
            _and_3 = _notEquals_1;
          }
          if (!_and_3) {
            _and_2 = false;
          } else {
            String _name_2 = mod.getName();
            boolean _notEquals_2 = (!Objects.equal(_name_2, "DISCRETE_TIME"));
            _and_2 = _notEquals_2;
          }
          if (!_and_2) {
            _and_1 = false;
          } else {
            String _name_3 = mod.getName();
            boolean _notEquals_3 = (!Objects.equal(_name_3, "MAUDELING"));
            _and_1 = _notEquals_3;
          }
          if (!_and_1) {
            _and = false;
          } else {
            String _name_4 = mod.getName();
            boolean _notEquals_4 = (!Objects.equal(_name_4, "OCL"));
            _and = _notEquals_4;
          }
          if (_and) {
            _builder.newLine();
            _builder.append("mod ");
            String _name_5 = mod.getName();
            _builder.append(_name_5, "");
            _builder.append(" is");
            _builder.newLineIfNotEmpty();
            _builder.append("  ");
            CharSequence _printModule = this.printModule(mod);
            _builder.append(_printModule, "  ");
            _builder.newLineIfNotEmpty();
            _builder.append("endm ---- end system module ");
            String _name_6 = mod.getName();
            _builder.append(_name_6, "");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  public CharSequence printModule(final Module mod) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _printModuleImportations = this.printModuleImportations(mod);
    _builder.append(_printModuleImportations, "");
    _builder.newLineIfNotEmpty();
    {
      EList<ModElement> _els = mod.getEls();
      Iterable<ModImportation> _filter = Iterables.<ModImportation>filter(_els, ModImportation.class);
      int _size = IterableExtensions.size(_filter);
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        _builder.append("  ");
        _builder.newLine();
      }
    }
    CharSequence _printSorts = this.printSorts(mod);
    _builder.append(_printSorts, "");
    _builder.newLineIfNotEmpty();
    {
      EList<ModElement> _els_1 = mod.getEls();
      Iterable<Sort> _filter_1 = Iterables.<Sort>filter(_els_1, Sort.class);
      int _size_1 = IterableExtensions.size(_filter_1);
      boolean _greaterThan_1 = (_size_1 > 0);
      if (_greaterThan_1) {
        _builder.append("  ");
        _builder.newLine();
      }
    }
    CharSequence _printSubSorts = this.printSubSorts(mod);
    _builder.append(_printSubSorts, "");
    _builder.newLineIfNotEmpty();
    {
      EList<ModElement> _els_2 = mod.getEls();
      Iterable<SubsortRel> _filter_2 = Iterables.<SubsortRel>filter(_els_2, SubsortRel.class);
      int _size_2 = IterableExtensions.size(_filter_2);
      boolean _greaterThan_2 = (_size_2 > 0);
      if (_greaterThan_2) {
        _builder.append("  ");
        _builder.newLine();
      }
    }
    CharSequence _printOps = this.printOps(mod);
    _builder.append(_printOps, "");
    _builder.newLineIfNotEmpty();
    {
      EList<ModElement> _els_3 = mod.getEls();
      Iterable<Operation> _filter_3 = Iterables.<Operation>filter(_els_3, Operation.class);
      int _size_3 = IterableExtensions.size(_filter_3);
      boolean _greaterThan_3 = (_size_3 > 0);
      if (_greaterThan_3) {
        _builder.append("  ");
        _builder.newLine();
      }
    }
    CharSequence _printEqs = this.printEqs(mod);
    _builder.append(_printEqs, "");
    _builder.newLineIfNotEmpty();
    {
      EList<ModElement> _els_4 = mod.getEls();
      Iterable<Equation> _filter_4 = Iterables.<Equation>filter(_els_4, Equation.class);
      int _size_4 = IterableExtensions.size(_filter_4);
      boolean _greaterThan_4 = (_size_4 > 0);
      if (_greaterThan_4) {
        _builder.append("  ");
        _builder.newLine();
      }
    }
    CharSequence _printRls = this.printRls(mod);
    _builder.append(_printRls, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence header() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("---- ----------------------------------------------- ----");
    _builder.newLine();
    _builder.append("---- Programmatically generated code using e-Motions ----");
    _builder.newLine();
    _builder.append("---- @date ");
    Date _date = new Date();
    _builder.append(_date, "");
    _builder.append("             ----");
    _builder.newLineIfNotEmpty();
    _builder.append("---- ----------------------------------------------- ----");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence printSorts(final Module mod) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ModElement> _els = mod.getEls();
      Iterable<Sort> _filter = Iterables.<Sort>filter(_els, Sort.class);
      for(final Sort s : _filter) {
        _builder.append("sort ");
        String _name = s.getName();
        _builder.append(_name, "");
        _builder.append(" .");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence printSubSorts(final Module mod) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ModElement> _els = mod.getEls();
      Iterable<SubsortRel> _filter = Iterables.<SubsortRel>filter(_els, SubsortRel.class);
      for(final SubsortRel ss : _filter) {
        _builder.append("subsort");
        {
          EList<Sort> _subsorts = ss.getSubsorts();
          for(final Sort subs : _subsorts) {
            _builder.append(" ");
            String _name = subs.getName();
            _builder.append(_name, "");
          }
        }
        _builder.append(" <");
        {
          EList<Sort> _supersorts = ss.getSupersorts();
          for(final Sort supers : _supersorts) {
            _builder.append(" ");
            String _name_1 = supers.getName();
            _builder.append(_name_1, "");
          }
        }
        _builder.append(" .");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence printOps(final Module mod) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ModElement> _els = mod.getEls();
      Iterable<Operation> _filter = Iterables.<Operation>filter(_els, Operation.class);
      for(final Operation op : _filter) {
        _builder.append("op ");
        String _name = op.getName();
        _builder.append(_name, "");
        _builder.append(" :");
        {
          EList<Type> _arity = op.getArity();
          for(final Type a : _arity) {
            _builder.append(" ");
            String _name_1 = a.getName();
            _builder.append(_name_1, "");
          }
        }
        _builder.append(" -> ");
        Type _coarity = op.getCoarity();
        String _name_2 = _coarity.getName();
        _builder.append(_name_2, "");
        _builder.append(" ");
        {
          EList<String> _atts = op.getAtts();
          boolean _hasElements = false;
          for(final String att : _atts) {
            if (!_hasElements) {
              _hasElements = true;
              _builder.append("[", "");
            } else {
              _builder.appendImmediate(" ", "");
            }
            _builder.append(att, "");
          }
          if (_hasElements) {
            _builder.append("]", "");
          }
        }
        _builder.append(" .");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence printModuleImportations(final Module mod) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ModElement> _els = mod.getEls();
      Iterable<ModImportation> _filter = Iterables.<ModImportation>filter(_els, ModImportation.class);
      for(final ModImportation imp : _filter) {
        {
          ImportationMode _mode = imp.getMode();
          boolean _equals = Objects.equal(_mode, ImportationMode.PROTECTING);
          if (_equals) {
            _builder.append("pr ");
          } else {
            ImportationMode _mode_1 = imp.getMode();
            boolean _equals_1 = Objects.equal(_mode_1, ImportationMode.INCLUDING);
            if (_equals_1) {
              _builder.newLineIfNotEmpty();
              _builder.append("inc ");
            } else {
              _builder.newLineIfNotEmpty();
              _builder.append("ex ");
            }
          }
        }
        ModExpression _imports = imp.getImports();
        CharSequence _printImportRelation = this.printImportRelation(_imports);
        _builder.append(_printImportRelation, "");
        _builder.append(" .");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence printImportRelation(final ModExpression exp) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((exp instanceof ModuleIdModExp)) {
        Module _module = ((ModuleIdModExp)exp).getModule();
        String _name = _module.getName();
        _builder.append(_name, "");
      }
    }
    return _builder;
  }
  
  public CharSequence printEqs(final Module mod) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ModElement> _els = mod.getEls();
      Iterable<Equation> _filter = Iterables.<Equation>filter(_els, Equation.class);
      for(final Equation eq : _filter) {
        {
          EList<Condition> _conds = eq.getConds();
          int _size = _conds.size();
          boolean _equals = (_size == 0);
          if (_equals) {
            _builder.append("eq ");
            
            String _label = eq.getLabel();
            if(_label != null && !_label.equals("")){
            	_builder.append("[");
            	_builder.append(_label);
            	_builder.append("] : ");
            }
            
            Term _lhs = eq.getLhs();
            CharSequence _printTerm = this.printTerm(_lhs);
            _builder.append(_printTerm, "");
            _builder.newLineIfNotEmpty();
            _builder.append("  ");
            _builder.append("= ");
            Term _rhs = eq.getRhs();
            CharSequence _printTerm_1 = this.printTerm(_rhs);
            _builder.append(_printTerm_1, "  ");
            _builder.append(" ");
            EList<String> _atts = eq.getAtts();
            CharSequence _printAtts = this.printAtts(_atts);
            _builder.append(_printAtts, "  ");
            _builder.append(".");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("ceq ");
            
            String _label = eq.getLabel();
            if(_label != null && !_label.equals("")){
            	_builder.append("[");
            	_builder.append(_label);
            	_builder.append("] : ");
            }
            
            Term _lhs_1 = eq.getLhs();
            CharSequence _printTerm_2 = this.printTerm(_lhs_1);
            _builder.append(_printTerm_2, "");
            _builder.newLineIfNotEmpty();
            _builder.append("  ");
            _builder.append("= ");
            Term _rhs_1 = eq.getRhs();
            CharSequence _printTerm_3 = this.printTerm(_rhs_1);
            _builder.append(_printTerm_3, "  ");
            _builder.newLineIfNotEmpty();
            _builder.append("  ");
            _builder.append("if ");
            EList<Condition> _conds_1 = eq.getConds();
            CharSequence _printConds = this.printConds(_conds_1);
            _builder.append(_printConds, "  ");
            _builder.append(" ");
            EList<String> _atts_1 = eq.getAtts();
            CharSequence _printAtts_1 = this.printAtts(_atts_1);
            _builder.append(_printAtts_1, "  ");
            _builder.append(".");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  public CharSequence printAtts(final EList<String> atts) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _hasElements = false;
      for(final String a : atts) {
        if (!_hasElements) {
          _hasElements = true;
          _builder.append("[", "");
        } else {
          _builder.appendImmediate(" ", "");
        }
        _builder.append(a, "");
      }
      if (_hasElements) {
        _builder.append("] ", "");
      }
    }
    return _builder;
  }
  
  public CharSequence printRlTerm(final Term term) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((term instanceof Constant)) {
        CharSequence _printConstant = this.printConstant(((Constant) term));
        _builder.append(_printConstant, "");
      } else {
        if ((term instanceof Variable)) {
          _builder.newLineIfNotEmpty();
          CharSequence _printVariable = this.printVariable(((Variable) term));
          _builder.append(_printVariable, "");
        } else {
          _builder.newLineIfNotEmpty();
          CharSequence _printRlRecTerm = this.printRlRecTerm(((RecTerm) term));
          _builder.append(_printRlRecTerm, "");
        }
      }
    }
    return _builder;
  }
  
  public CharSequence printTerm(final Term term) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((term instanceof Constant)) {
        CharSequence _printConstant = this.printConstant(((Constant) term));
        _builder.append(_printConstant, "");
      } else {
        if ((term instanceof Variable)) {
          _builder.newLineIfNotEmpty();
          CharSequence _printVariable = this.printVariable(((Variable) term));
          _builder.append(_printVariable, "");
        } else {
          _builder.newLineIfNotEmpty();
          CharSequence _printRecTerm = this.printRecTerm(((RecTerm) term));
          _builder.append(_printRecTerm, "");
        }
      }
    }
    return _builder;
  }
  
  public CharSequence printRecTerm(final RecTerm term) {
    StringConcatenation _builder = new StringConcatenation();
    String _op = term.getOp();
    _builder.append(_op, "");
    {
      EList<Term> _args = term.getArgs();
      boolean _hasElements = false;
      for(final Term a : _args) {
        if (!_hasElements) {
          _hasElements = true;
          _builder.append("(", "");
        } else {
          _builder.appendImmediate(", ", "");
        }
        {
          String _op_1 = term.getOp();
          boolean _equals = Objects.equal(_op_1, "_`{_`}");
          if (_equals) {
            CharSequence _printRlTerm = this.printRlTerm(a);
            _builder.append(_printRlTerm, "");
          } else {
            Object _printTerm = this.printTerm(a);
            _builder.append(_printTerm, "");
          }
        }
      }
      if (_hasElements) {
        _builder.append(")", "");
      }
    }
    return _builder;
  }
  
  public CharSequence printRlRecTerm(final RecTerm term) {
    StringConcatenation _builder = new StringConcatenation();
    {
      String _op = term.getOp();
      boolean _equals = Objects.equal(_op, "<_:_|_>");
      if (_equals) {
        _builder.append("\n    ", "");
        String _op_1 = term.getOp();
        _builder.append(_op_1, "");
      } else {
        String _op_2 = term.getOp();
        boolean _equals_1 = Objects.equal(_op_2, "__");
        if (_equals_1) {
          _builder.newLineIfNotEmpty();
          _builder.append("\n  ", "");
          String _op_3 = term.getOp();
          _builder.append(_op_3, "");
        } else {
          String _op_4 = term.getOp();
          boolean _equals_2 = Objects.equal(_op_4, "_#_");
          if (_equals_2) {
            _builder.newLineIfNotEmpty();
            _builder.append("\n      ", "");
            String _op_5 = term.getOp();
            _builder.append(_op_5, "");
          } else {
            String _op_6 = term.getOp();
            boolean _equals_3 = Objects.equal(_op_6, "_:_");
            if (_equals_3) {
              _builder.newLineIfNotEmpty();
              _builder.append("\n        ", "");
              String _op_7 = term.getOp();
              _builder.append(_op_7, "");
            } else {
              String _op_8 = term.getOp();
              _builder.append(_op_8, "");
            }
          }
        }
      }
    }
    {
      EList<Term> _args = term.getArgs();
      boolean _hasElements = false;
      for(final Term a : _args) {
        if (!_hasElements) {
          _hasElements = true;
          _builder.append("(", "");
        } else {
          _builder.appendImmediate(", ", "");
        }
        Object _printRlTerm = this.printRlTerm(a);
        _builder.append(_printRlTerm, "");
      }
      if (_hasElements) {
        _builder.append(")", "");
      }
    }
    return _builder;
  }
  
  public CharSequence printRls(final Module mod) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ModElement> _els = mod.getEls();
      Iterable<Rule> _filter = Iterables.<Rule>filter(_els, Rule.class);
      for(final Rule rl : _filter) {
        {
          EList<Condition> _conds = rl.getConds();
          int _size = _conds.size();
          boolean _greaterThan = (_size > 0);
          if (_greaterThan) {
            _builder.append("crl");
          } else {
            _builder.append("rl");
          }
        }
        _builder.append(" [");
        String _label = rl.getLabel();
        _builder.append(_label, "");
        _builder.append("] :");
        _builder.newLineIfNotEmpty();
        _builder.append("  ");
        Term _lhs = rl.getLhs();
        CharSequence _printRlTerm = this.printRlTerm(_lhs);
        _builder.append(_printRlTerm, "  ");
        _builder.newLineIfNotEmpty();
        _builder.append("=>");
        _builder.newLine();
        _builder.append("  ");
        Term _rhs = rl.getRhs();
        CharSequence _printRlTerm_1 = this.printRlTerm(_rhs);
        _builder.append(_printRlTerm_1, "  ");
        {
          EList<Condition> _conds_1 = rl.getConds();
          int _size_1 = _conds_1.size();
          boolean _equals = (_size_1 == 0);
          if (_equals) {
            _builder.append(" .");
          } else {
            _builder.newLineIfNotEmpty();
            _builder.append("if ");
            EList<Condition> _conds_2 = rl.getConds();
            CharSequence _printConds = this.printConds(_conds_2);
            _builder.append(_printConds, "");
            _builder.append(" [print \" -> ");
            String _label_1 = rl.getLabel();
            _builder.append(_label_1, "");
            _builder.append(" \\tin time \" TIME@CLK@:Time] .");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public String cleanSpaces(final String string) {
    String res = string;
    int _indexOf = res.indexOf("  ");
    boolean _notEquals = (_indexOf != (-1));
    boolean _while = _notEquals;
    while (_while) {
      String _replaceAll = res.replaceAll("  ", " ");
      res = _replaceAll;
      int _indexOf_1 = res.indexOf("  ");
      boolean _notEquals_1 = (_indexOf_1 != (-1));
      _while = _notEquals_1;
    }
    return res;
  }
  
  public CharSequence printConstant(final Constant c) {
    StringConcatenation _builder = new StringConcatenation();
    String _op = c.getOp();
    _builder.append(_op, "");
    return _builder;
  }
  
  public CharSequence printVariable(final Variable v) {
    StringConcatenation _builder = new StringConcatenation();
    {
      Type _type = v.getType();
      boolean _notEquals = (!Objects.equal(_type, null));
      if (_notEquals) {
        String _name = v.getName();
        _builder.append(_name, "");
        _builder.append(":");
        Type _type_1 = v.getType();
        String _name_1 = _type_1.getName();
        _builder.append(_name_1, "");
      } else {
        String _name_2 = v.getName();
        _builder.append(_name_2, "");
      }
    }
    return _builder;
  }
  
  public CharSequence printConds(final EList<Condition> list) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _hasElements = false;
      for(final Condition t : list) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate("\n/\\ ", "");
        }
        CharSequence _printCondition = this.printCondition(t);
        _builder.append(_printCondition, "");
      }
    }
    return _builder;
  }
  
  public CharSequence printCondition(final Condition cond) {
    CharSequence _xifexpression = null;
    if ((cond instanceof MatchingCond)) {
      _xifexpression = this.printMatchingCond(((MatchingCond) cond));
    } else {
      CharSequence _xifexpression_1 = null;
      if ((cond instanceof BooleanCond)) {
        Term _lhs = ((BooleanCond)cond).getLhs();
        _xifexpression_1 = this.printTerm(_lhs);
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public CharSequence printMatchingCond(final MatchingCond cond) {
    StringConcatenation _builder = new StringConcatenation();
    Term _lhs = cond.getLhs();
    CharSequence _printTerm = this.printTerm(_lhs);
    _builder.append(_printTerm, "");
    _builder.append(" := ");
    Term _rhs = cond.getRhs();
    CharSequence _printRlTerm = this.printRlTerm(_rhs);
    _builder.append(_printRlTerm, "");
    return _builder;
  }
}