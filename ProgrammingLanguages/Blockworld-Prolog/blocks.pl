%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Daniel Campos 
% RIN: 660996361
% RCS:campod2
% Zoe Konrad
% RIN: 660988052
% RCS:konraz
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Command idiom c:
%  {Please} place <put> {block} X on <onto >{block} Y, W on Z, ...
%  I want <would like> X on Y, W on Z, ...
%  I want <would like> you to put <place> ...
%  Can <could> <would> you {please} put <place> X on Y, ...
%  put all of the blocks in a single pile.
%  Put the block on top of X on top of block Y.
%  Put the highest block on top of block y.

c(L) --> lead_in,arrange(L),end.

end --> ['.'] | ['?'].

lead_in --> please, place.
lead_in --> [i], [want] | [i], [would], [like], you_to_put.
lead_in --> ([can] | [could] | [would]), [you], please, place.

you_to_put --> [] | [you], [to], place.   %%% partially optional

please --> [] | [please].    %%% optional word

place --> [put] | [place].   %%% alternate words
comma --> [','] | ['and'] | [','],[and].   %%% alternate words
motion --> [on] | [onto] | [on],[top],[of]. % i wanted to shorten
arrange([ON]) --> on(ON).
arrange([ON|R]) --> on(ON), comma, arrange(R).
arrange([IN]) --> in(IN). %find uses of in
arrange([IN]|R) --> in(IN), comma, arrange(R).
arrange([THE]) --> the(THE). %find uses of THE
arrange([THE]|R) --> the(THE), comma, arrange(R).

on(on_begin_result(Y)) --> [result], motion, block, [Y].
on(on_end_result(X)) --> [block], [X], motion, [result].
on(on_table_result(table)) --> [result],motion,[the],[table].
on(on(X,Y)) --> block, [X], motion, block, [Y].
on(on(X,table)) --> [X],motion, [the], [table].
on(result_on_top(B)) --> [the],block,motion,[result],motion,block,[B].
on(top_of_result(A)) --> [the],block,motion,[A],motion,block,[result].
in(in(single,pile)) --> [all], [of], [the], [blocks], [in], [a], [single], [pile]. 

the(ontopof(X,Y)) --> [the],[block],motion,[X],motion,block,[Y].
the(ontopof(X,table)) --> [the],[block],motion,[X],motion,[the],[table].

the(highest(table)) --> [the],[highest],block,motion,[the],[table].
the(highest(Y)) --> [the],[highest],block,motion,block,[Y].
the(highest_on_result) --> [the],[highest],block,motion,[result].

block --> [] | [block].   %%% optional word

:- [read_line].
place_blocks :- 
    repeat,
    write('?? '),
    read_line(X),
    (c(F,X,[]),assert_list(F), write('ok.'), nl | q(F,X,[]) ),
    answer(F), nl, fail.

% Assert each item in the list.
assert_list([]).
assert_list([H|T]) :- assert_item(H), assert_list(T).

% Add a new table spot.
assert_table_spot([X,0]) :- assert(free_spot_on_table([X,0])).
assert_table_spot([_,Y]) :- Y \= 0.

% Handle errors.
assert_item(on(A,table)) :- 
    location(A,[_,Y]), Y is 0,
    write('Already on the table!'), nl, !, fail.
assert_item(on(A,table)) :- 
    location(A,[X,Y]), YN is Y + 1, location(_,[X,YN]),
    write('Cannot move from, something is on top!'), nl, !, fail.
assert_item(on(A,_)) :-
    not(location(A, _)),
    A \== result,
    write('Block to move does not exist!'), nl, !, fail.
assert_item(on(_,B)) :-
    B \== table, not(location(B, _)),
    B \== result,
    write('Block to place on does not exist!'), nl, !, fail.
assert_item(on(A,B)) :- 
    B \== table, location(A, [XA,YA]), YAN is YA + 1, location(_, [XA,YAN]),
    write('Cannot move from, something is on top!'), nl, !, fail.
assert_item(on(_,B)) :- 
    B \== table, location(B, [XB,YB]), YBN is YB + 1, location(_, [XB,YBN]),
    write('Cannot move to, something is on top!'), nl, !, fail.

% Move block A on the table.
assert_item(on(A,table)) :- 
    A \== result, % make sure we dont use this one to move result
    location(A,[X,Y]),
    not(Y is 0),
    free_spot_on_table(P), 
    YN is Y + 1,
    not(location(_,[X,YN])),
    retract(free_spot_on_table(P)), 
    retract(location(A,[X,Y])),
    assert(location(A,P)),!,
    nb_setval(result, A).
% Move block A on block B.
assert_item(on(A,B)) :- 
    A \== result,
    B \== result,
    B \== table,
    location(A, [XA,YA]),
    YAN is YA + 1,
    not(location(_, [XA,YAN])),
    location(B, [XB,YB]),
    YBN is YB + 1,
    not(location(_, [XB,YBN])),
    retract(location(A, [XA,YA])),
    assert_table_spot([XA,YA]), % Possibly free up spot on table.
    assert(location(A, [XB,YBN])),!,
    nb_setval(result, A).    

% Part 1D)On top of Y
assert_item(ontopof(A,B)) :- 
    A \== result,
    B \== result,
    B \== table,
    not(on(_,B)),
    on(Y,A),
    not(on(_,Y)),
    assert_item(on(Y,B)),
    nb_setval(result, Y).
    
% Part 1D)On top of table
assert_item(ontopof(X,table)) :- 
    X \== result,
    on(Y,X),
    not(on(_,Y)),
    assert_item(on(Y,table)),
    nb_setval(result, Y).
% Part 1E) highest toY
assert_item(highest(Y)) :- 
    findall(YN, location(_, [_,YN]), L),
    slow_max(L, M),
    findall(B, location(B, [_,M]), K),
    last(K, A), %this may be dirty but it works. we get all the Y values, get a max, and then find the first block with said y value 
    assert_item(on(A,Y)),
    nb_setval(result, A).

% Part 1C)Single Pile
assert_item(in(single,pile)) :-
    not(single_pile_maker).

% Part 2! 
% put result on B
assert_item(on_begin_result(B)) :-
    nb_getval(result, A),
    B \== table,
    location(A, [XA,YA]),
    YAN is YA + 1,
    not(location(_, [XA,YAN])),
    location(B, [XB,YB]),
    YBN is YB + 1,
    not(location(_, [XB,YBN])),
    retract(location(A, [XA,YA])),
    assert_table_spot([XA,YA]),
    assert(location(A, [XB,YBN])),!,
    nb_setval(result, A).

% Put A on result
assert_item(on_end_result(A)) :-
    nb_getval(result, B),
    B \== table,
    location(A, [XA,YA]),
    YAN is YA + 1,
    not(location(_, [XA,YAN])),
    location(B, [XB,YB]),
    YBN is YB + 1,
    not(location(_, [XB,YBN])),
    retract(location(A, [XA,YA])),
    assert_table_spot([XA,YA]),
    assert(location(A, [XB,YBN])),!,
    nb_setval(result, B).

% Put the result on the table
assert_item(on_table_result(table)) :-
    nb_getval(result, A),
    location(A,[X,Y]),
    not(Y is 0),
    free_spot_on_table(P), 
    YN is Y + 1,
    not(location(_,[X,YN])),
    retract(free_spot_on_table(P)), 
    retract(location(A,[X,Y])),
    assert(location(A,P)),!,
    nb_setval(result, A).
% put the result on top of B
assert_item(result_on_top(B)) :-
    nb_getval(result, A),
    B\== table,
    not(on(_,B)),
    on(Y,A),
    not(on(_,Y)),
    assert_item(on(Y,B)),
    nb_setval(result, Y).
% put A on top of result
assert_item(top_of_result(A)) :-   
    nb_getval(result, B),
    B\== table,
    not(on(_,B)),
    on(Y,A),
    not(on(_,Y)),
    assert_item(on(Y,B)),
    nb_setval(result, Y).
% put the highest on result
assert_item(highest_on_result) :-
    nb_getval(result, Y),
    findall(YN, location(_, [_,YN]), L),
    slow_max(L, M),
    findall(B, location(B, [_,M]), K),
    last(K, A),
    assert_item(on(A,Y)),
    nb_setval(result, A).



% has to be after assert so no warnings
% Part 1C) Single pile we find all the blocks on the table and remove them and pass em to our helper
single_pile_maker :-
    findall(B, location(B, [_,_]), L),
    retractall(location(_, _)),
    put_single_pile(L, 0).
put_single_pile([], _):-
    fail.
put_single_pile([H|T], Y):-
    assert(location(H,[0,Y])),
    Y1 is Y + 1,
    put_single_pile(T, Y1).
slow_max(L, Max) :-
    select(Max, L, Rest), \+ (member(E, Rest), E > Max).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Question idiom q:
%   Which block is on top of X?
%   What is on top of?
%   What is block X sitting on?
%   Which blocks are on the table?
:- op(500, xfx, 'is_on_top_of_result').
:- op(500, xfx, 'result_sitting_on').
:- op(500, xfx, 'is_on_top_of').
:- op(500, xfx, 'sitting_on').
:- op(500, xfx, 'on_table').
% the question q
q(is_on_top_of_result(_)) --> [what],[is],[on],[top],[of],[result],end.
q(is_on_top_of_result(_)) --> [which],[block],[is],[on],[top],[of],[result],end.
q(result_sitting_on(_)) --> [what],[is],[result],[sitting],[on],end.
q(_ is_on_top_of A) --> [which],[block],[is],[on],[top],[of],[A],end.
q(_ is_on_top_of A) --> [what],[is],[on],[top],[of],[A],end.
q(A sitting_on _) --> [what],[is],[block],[A],[sitting],[on],end.
q(on_table(_)) --> [which],[blocks],[are],[on],[the],[table],end.

% How to answer q
is_on_top_of_result(B):-
    nb_getval(result,A),
    location(A,[X,Y]),
    Y1 is Y+1,
    location(B,[X,Y1]), !.
is_on_top_of_result('Nothing').
result_sitting_on(B) :- 
    nb_getval(result,A),
    location(A,[X,Y]),
    Y1 is Y-1,
    location(B,[X,Y1]), !.
result_sitting_on('the table') .
B is_on_top_of A :- 
    B \== result,
    A \== result,
    location(A,[X,Y]),
    Y1 is Y+1,
    location(B,[X,Y1]), !.
'Nothing' is_on_top_of _ .
A sitting_on B :- 
    B \== result,
    A \== result,
    location(A,[X,Y]),
    Y1 is Y-1,
    location(B,[X,Y1]), !.
_ sitting_on 'the table' .
on_table(L) :- 
    findall(X, on(X, table), L). 

answer(is_on_top_of_result(X)) :- 
    call(is_on_top_of_result(X)),
    say([X,is,on,top,of,result]),
    nb_setval(result, X).
answer(result_sitting_on(X)) :- 
    call(result_sitting_on(X)),
    say([result,is,sitting,on,X]),
    nb_setval(result,X).
answer(X is_on_top_of A) :- 
    A \== result,
    call(X is_on_top_of A),
    say([X,is,on,top,of,A]),
    nb_setval(result, X).
answer(A sitting_on X) :- 
    A \== result,
    call(A sitting_on X),
    say([A,is,sitting,on,X]),
    nb_setval(result, X).
answer(on_table(L)) :- 
    call(on_table(L)),
    say([L]).

say([X|R]) :- write(X), write(' '), say(R).
say([]).

%
%  positioning information
%
%  [0,3] [1,3] [2,3]
%  [0,2] [1,2] [2,2]
%  [0,1] [1,1] [2,1]
%  [0,0] [1,0] [2,0]
% -=================-   table
%   
% initially 
%
%    c
%    b
%    a     d
% -=================-

:- dynamic free_spot_on_table/1.
:- dynamic location/2.
:- dynamic on/2.

free_spot_on_table([2,0]).
 
location(c,[0,2]).
location(b,[0,1]).
location(a,[0,0]).
location(d,[1,0]).

on(A,table) :- location(A,[_,0]).
on(A,B) :- B \== table,
           location(A,[X,YA]),
           location(B,[X,YB]),
           YB is YA - 1.
