from pygments.lexer import RegexLexer
from pygments.token import *

from sphinx.highlighting import lexers

from pygments.lexers.configs import PropertiesLexer
from pygments.lexers.html import XmlLexer
from pygments.lexers.jvm import GroovyLexer
# used the JavaLexer as basis

class IrlLexer(RegexLexer):
   name = 'irl'
   aliases = [ 'irl' ]
   filenames = [ '*.irl' ]
   
   tokens = {
      'root': [
            (r'(package|import|model|sub|enum|template|abstract|event|entity|extends|const|alias|as|grouped|by|transient|auto-increment|changeable)\b', Keyword.Type),
            (r'(boolean|byte|char|double|float|int|long|short|void)\b', Keyword.Type),
            (r'(true|false)\b', Keyword.Constant),
            (r'"', String, 'string'),
            (r"'\\.'|'[^\\]'|'\\u[0-9a-fA-F]{4}'", String.Char),
            (r'[^\S\n]+', Text),
            (r'//.*?\n', Comment.Single),
            (r'/\*.*?\*/', Comment.Multiline),
            (r'(@author|@since)\b', Name.Decorator),
            (r'0|[1-9][0-9_]*[lL]?', Number.Integer),
            (r'([^\W\d]|\$)[\w$]*', Name),
            (r'[,:\[\]\{\}=]', Operator),
            (r'\n', Text)
      ],
      'string': [
            (r'[^\\"]+', String),
            (r'\\\\', String),  # Escaped backslash
            (r'\\"', String),  # Escaped quote
            (r'\\', String),  # Bare backslash
            (r'"', String, '#pop'),  # Closing quote
      ]
   }

lexers['irl'] = IrlLexer()
lexers['properties'] = PropertiesLexer()
lexers['xml'] = XmlLexer()
lexers['gradle'] = GroovyLexer()
