from pprint import pprint

import copy

import time


def timing(f,shouldTime=True):
    def wrap(*args):
        time1 = time.time()
        ret = f(*args)
        time2 = time.time()
        print '%s function took %0.3f ms' % (f.func_name, (time2-time1)*1000.0)
        return ret
    return wrap

class PegGame:
    peg=1
    empty=0
    out_bounds=2
    dist=2
    time=False
    def __init__(self,mpeg_loc=(0,4),pattern=None):
        self.mpeg_loc = mpeg_loc
        if not pattern:
            self._setup()
        else: self.pattern=pattern

    #@timing
    def _setup(self, width=5):
        w=width*2-1
        pattern=[self.out_bounds] * width
        for i in range(len(pattern)): pattern[i] = [self.out_bounds] * w
        for i in range(len(pattern)):
            for j in range(len(pattern)-i-1,len(pattern)+i,2):
                if j%2==i%2:
                    pattern[i][j]=self.peg
        self.pattern = pattern
        self.removePeg(*self.mpeg_loc)
        return pattern

    #@timing
    def putPeg(self, x, y):
        if self.pattern[x][y] == self.out_bounds:
            self.pattern[x][y] = 9
            raise Exception('Can\'t put peg at ({x},{y}), out of bounds\n{board}'
                            .format(x=x,y=y,board=self.printPattern()))
        if self.pattern[x][y] == self.peg:
            self.pattern[x][y] = 9
            raise Exception('Can\'t put peg at ({x},{y}), peg already there\n{board}'
                            .format(x=x,y=y,board=self.printPattern()))
        self.pattern[x][y]=self.peg

    #@timing
    def removePeg(self, x, y):
        if self.pattern[x][y] == self.out_bounds:
            self.pattern[x][y] = 9
            raise Exception('Can\'t remove peg from ({x},{y}), out of bounds\n{board}'
                            .format(x=x,y=y,board=self.printBoard()))
        if self.pattern[x][y] == self.empty:
            raise Exception('Can\'t remove peg from ({x},{y}), no peg there\n{board}'
                            .format(x=x,y=y,board=self.printBoard()))
        self.pattern[x][y]=self.empty

    #@timing
    def jumpPeg(self,from_loc,to_loc):
        if type(from_loc) is not tuple or type(to_loc) is not tuple:
            raise Exception('jumpPeg method require from_loc and to_loc to be tuples\n\t'
                            'provided: from_loc = {f} ({ft}), to_loc = {t} ({tt})'
                            .format(f=str(from_loc),t=str(to_loc),ft=type(from_loc),tt=type(to_loc)))
        # throw exception if from_loc to to_loc is not a distance of 2
        if not self.canJumpPeg(from_loc,to_loc):
            self.pattern[from_loc[0]][from_loc[1]]=9
            self.pattern[to_loc[0]][to_loc[1]]=8
            raise Exception('Peg distance should be {dist}\nprovided: from_loc = {f}, to_loc = {t}, distance = {d}\n{board}'
                            .format(dist=self.dist,f=str(from_loc),t=str(to_loc),d=str(self.getDistance(from_loc,to_loc)),board=self.printBoard()))
        self.removePeg(*from_loc)
        # remove peg in between (a-b)/2+b
        self.removePeg(*self.getSpotBetween(from_loc,to_loc))
        self.putPeg(*to_loc)

    #@timing
    def getSpotBetween(self,from_loc,to_loc):
        return map(lambda x,y: (x-y)/2+y, from_loc,to_loc)

    #@timing
    def getDistance(self, from_loc, to_loc):
        return map(lambda x,y: abs(x-y)/2., from_loc,to_loc)

    #@timing
    def canJumpPeg(self, from_loc, to_loc):
        d = self.getDistance(from_loc,to_loc)
        if d[0] == 0 and d[1] == self.dist:
            return d[1] == self.dist
        return (d[0] == self.dist/2 and d[0] > 0) and (d[1] == self.dist/2 and d[1] > 0)

    #@timing
    def getAllAllowedMoves(self):
        pegs=[]; empties = []
        for x in range(len(self.pattern)):
            for y in range(len(self.pattern[x])):
                if self.pattern[x][y] == self.peg:
                    pegs.append((x,y))
                elif self.pattern[x][y] == self.empty:
                    empties.append((x,y))
        allowed={}
        for peg in pegs:
            for empty in empties:
                l=self.getSpotBetween(peg,empty)
                if self.canJumpPeg(peg,empty) and self.pattern[l[0]][l[1]] != self.empty:
                    if peg in allowed:
                        allowed[peg].append(empty)
                    else: allowed[peg]=[empty]
        return allowed

    #@timing
    def getAllAllowedMovesExpanded(self):
        am = self.getAllAllowedMoves()
        allowed=[]
        for peg, empties in am.iteritems():
            for empty in empties:
                allowed.append({'peg':peg,'empty':empty})
        return allowed

    #@timing
    def printBoard(self):
        b=""
        for i in range(len(self.pattern)):
            for j in range(len(self.pattern[i])):
                if self.pattern[i][j]==self.out_bounds:
                    b+=' '
                    continue
                b+=str(self.pattern[i][j])
            b+='\n'
        return b

    #@timing
    def solve(self,goal=1):
        return self._solve(goal,self,[],0)

    #@timing
    def _solve(self,goal,pegGame,moves,depth):
        if len(pegGame.getAllAllowedMovesExpanded()) == 0:
            moves.append(pegGame)
            return moves
        sols=[]
        for pegEmpty in pegGame.getAllAllowedMovesExpanded():
            p=pegGame.clone()
            m=[]
            m.extend(moves)
            p.jumpPeg(pegEmpty['peg'],pegEmpty['empty'])
            m.append(pegEmpty)
            if len(p.getPegs()) == moves:
                return m
            mms=self._solve(goal,p,m,depth+1)
            if len(mms[-1].getPegs()) == goal:
                return mms
            sols.append(mms)
        best=sols[0]
        for sol in sols[1:]:
            if len(sol[-1].getPegs()) < len(best[-1].getPegs()):
                best = sol
        return best

    #@timing
    def clone(self):
        return PegGame(pattern=copy.deepcopy(self.pattern))

    #@timing
    def getPegs(self):
        return [(x,y) for x in range(len(self.pattern)) for y in range(len(self.pattern[x])) if self.pattern[x][y] == self.peg]

    def playGame(self):
        while self.getAllAllowedMoves():
            try:
                print(self.printBoard())
                pprint(self.getAllAllowedMoves())
                ft=raw_input("input from and to location as 4 numbers\nx1 y1 x2 y2: ").split()
                ft=[int(i) for i in ft]
                self.jumpPeg((ft[0],ft[1]),(ft[2],ft[3]))
            except Exception, e:
                print(e)
                continue
        print(self.printBoard())
        print("Game done, you had {n} pegs left".format(n=len(self.getPegs())))


if __name__ == '__main__':
    s= PegGame().solve()
    pprint(s)
    print(s[-1].printBoard())
    print(len(s[-1].getPegs()))
    # PegGame().playGame()
    pass