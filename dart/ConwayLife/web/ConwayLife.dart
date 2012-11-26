import 'dart:html';
import 'dart:isolate';
import 'dart:math';

void main() {
  new GamePresenter();
}

/**
 * Main game presenter.
 */
class GamePresenter {
  const int _cellSize = 10;
  bool _running = false;
  GameCanvas _canvas;
  LifeGame _game;
  Timer _timer;
  int _width, _height;

  GamePresenter() {
    _width = query("#canvas").clientWidth;
    _height = query("#canvas").clientHeight;
    _game = new LifeGame(_width ~/ _cellSize, _height ~/ _cellSize);
    _canvas = new GameCanvas(query("#canvas"), _width, _height, _cellSize)..onClick(toggle);
    query("#startButton").on.click.add((_) => _running ? stop() : start());
    query("#randomize").on.click.add((_) => randomize());
  }

  toggle(Point p) {
    _canvas.fill(_game.togglePoint(p));
  }

  randomize() {
    final random = new Random();
    _game.forEach((Point p) { if (random.nextBool()) toggle(p); });
  }

  start() {
    _running = true;
    _timer = new Timer.repeating(0, (_) => _game.next().forEach(_canvas.fill));
  }

  stop() {
    _running = false;
    _timer.cancel();
  }

}

/**
 * Game canvas.
 */
class GameCanvas {
  final int _cellSize, _width, _height;
  final CanvasElement _canvas;

  GameCanvas(this._canvas, this._width, this._height, this._cellSize) {
    _drawGrid();
  }

  onClick(f) {
    _canvas.on.click.add((MouseEvent event)
        => f(new Point((event.offsetX ~/ _cellSize), (event.offsetY ~/ _cellSize))));
  }

  fill(Cell cell) {
    final context = _canvas.context2d;
    context.setLineWidth(1);
    Point p = cell.point;
    if (cell.isAlive) {
      context.fillRect(p.x * _cellSize, p.y * _cellSize, _cellSize, _cellSize);
    } else {
      context.clearRect(p.x * _cellSize + 1, p.y * _cellSize + 1,
          _cellSize - 2, _cellSize - 2);
    }
  }

  _drawGrid() {
    final context = _canvas.context2d;
    context.setLineWidth(1);
    for (int i = 0; i <= _height / _cellSize; i++) {
      context.moveTo(0, i * _cellSize);
      context.lineTo(_width, i * _cellSize);
      context.stroke();
    }
    for (int i = 0; i <= _width / _cellSize; i++) {
      context.moveTo(i * _cellSize, 0);
      context.lineTo(i * _cellSize, _height);
      context.stroke();
    }
  }
}

/**
 * A two dimensional point.
 */
class Point {
  num x, y;
  Point(this.x, this.y);
}

/**
 * A game cell.
 */
class Cell {
  Point point;
  bool isAlive;
  Cell(this.point, this.isAlive);
}

/**
 * A simple quadratic O(height * width) implementation.
 */
class LifeGame {

  List<List<bool>> _state;
  final int _height, _width;

  LifeGame(this._width, this._height) {
    assert(_width >= 0);
    assert(_height >= 0);
    _state = new List(_width);
    for (int x = 0; x < _width; x++) {
      _state[x] = new List(_height);
      for (int y = 0; y < _height; y++) {
        _state[x][y] = false;
      }
    }
  }

  forEach(f) {
    for (int x = 0; x < _width; x++) {
      for (int y = 0; y < _height; y++) {
        f(new Point(x, y));
      }
    }
  }

  List<Cell> next() {
    final List<Cell> result = [];
    forEach((Point p) {
      bool newState = _nextState(p);
      if (_state[p.x][p.y] != newState)
        result.add(new Cell(p, newState));
    });
    result.forEach((Cell n) => togglePoint(n.point));
    return result;
  }

  Cell togglePoint(Point p) {
    assert(p.x >= 0 && p.x <= _width);
    assert(p.y >= 0 && p.y <= _height);
    _state[p.x][p.y] = !_state[p.x][p.y];
    return new Cell(p, _state[p.x][p.y]);
  }

  bool _nextState(Point p) {
    final num n = _countLiveNeighbours(p);
    if (_state[p.x][p.y]) {
      return (n == 2 || n == 3);
    } else {
      return (n == 3);
    }
  }

  num _countLiveNeighbours(Point p) => _nodeState(p.x - 1, p.y - 1) + _nodeState(p.x, p.y - 1)
    + _nodeState(p.x + 1, p.y - 1) + _nodeState(p.x - 1, p.y) + _nodeState(p.x + 1, p.y)
    + _nodeState(p.x - 1, p.y + 1) + _nodeState(p.x, p.y + 1) + _nodeState(p.x + 1, p.y + 1);

  num _nodeState(num x, y) => (x >= 0 && x < _width && y >= 0 && y < _height && _state[x][y]) ? 1 : 0;

}