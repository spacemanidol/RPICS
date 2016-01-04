declare Deck = [1 2 3 4 5 6 7 8 9 10 10 10 10
		1 2 3 4 5 6 7 8 9 10 10 10 10
		1 2 3 4 5 6 7 8 9 10 10 10 10
		1 2 3 4 5 6 7 8 9 10 10 10 10]
declare Random Init Max Next Length 
local A = 333667 B=213453321 M=1000000000 in
  proc {NewRand ?Rand ?Init ?Max}
    X = {NewCell 0} in
    fun {Rand} X := (A*@X+B) mod M end
    proc {Init Seed} X := Seed end
    Max = M
  end
end

%Representations %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%Table represenation
declare fun {MakeTableRepresentation DeckIn DeckOut PlayerMoney DealerMoney Random}
	   table(deckin:DeckIn deckout:DeckOut playermoney:PlayerMoney dealermoney:DealerMoney random:Random)
end

%Game representation
declare fun {MakeGameRepresentation DeckIn Deckout PlayerCards DealerCards Random}
	   game(deckin:DeckIn deckout:Deckout playercards:PlayerCards dealerhand:DealerCards random:Random)
end

%Deck Representation
declare fun{MakeHandRecord Random DeckIn DeckOut Hand Bet}
	   handrecord(random:Random deckin:DeckIn deckout:DeckOut hand:Hand bet:Bet)
end

declare fun{MakeShuffleRecord Random DeckIn}
	   shufflerecord(random:Random deckin:DeckIn)
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%Helper Function %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%Set All Players to Have The Same initial Money
declare fun {InitPlayerMoney Players Index InitialMoney}
	   if Index > Players then nil
	   else
	      InitialMoney | {InitPlayerMoney Players Index+1 InitialMoney}
	   end
end

%Gives hard score with ace valued at 11
declare fun {ReplaceHard K}
   if K == 1 then 11 else K end
end

%Adds up soft score(Ace as 1)
declare fun {HandValueSoft L}
	   case L
   of nil then 0
   [] X|L2 then X+{HandValueSoft L2}
   end
end

%Adds up hard score(Ace as 2)
declare fun {HandValueHard L}
	case L
   of nil then 0
   [] X|L2 then {ReplaceHard X}+{HandValueHard L2}
   end
end

%Based on hand, bet and money tells what bet to do
declare fun {PlayerMove Hand Money Bet}
	   if {HandValueHard Hand} == 21 then 'stand'
	   elseif {HandValueHard Hand} < 21 then
	      if {HandValueHard Hand} < 9 then 'hit' 
	      elseif {HandValueHard Hand} <12 then
		 if Money >= 2* Bet then 'double' else 'hit' end
	      elseif {HandValueHard Hand} < 17 then 'hit'
	      else 'stand'
	      end
	   elseif {HandValueSoft Hand} == 21 then 'stand'
	   elseif {HandValueSoft Hand} < 21 then
	      if {HandValueSoft Hand} < 9 then 'hit' 
	      elseif {HandValueSoft Hand} < 12 then
		 if Money > Bet then 'double' else 'hit' end
	      elseif {HandValueSoft Hand} < 17 then 'hit'
	      else 'stand' end
	   else 'stand'
	   end
end 

%Based on DealerHand and Playerhand the players outcome is decided
declare fun{GameOutcome PlayerHand DealerHand}
	   if {HandValueHard PlayerHand} < 22 then
	      if {HandValueHard DealerHand} > 21 then
		 if {HandValueSoft DealerHand} > 21 then
		    if {HandValueHard PlayerHand} == 21 then 'blackjack'
		    else 'win' end
		 elseif {HandValueSoft DealerHand} < {HandValueHard PlayerHand} then
		    if {HandValueHard PlayerHand} == 21 then 'blackjack'
		    else 'win' end
		 elseif {HandValueSoft DealerHand} == {HandValueHard PlayerHand} then 'tie'
		 else 'loss' end
	      elseif {HandValueHard DealerHand} < {HandValueHard PlayerHand} then
		 if {HandValueHard PlayerHand} == 21 then 'blackjack'
		 else 'win' end
	      elseif {HandValueHard DealerHand} == {HandValueHard PlayerHand} then 'tie'	 
	      else 'loss' end
	    elseif {HandValueSoft PlayerHand} < 22 then
	      if {HandValueHard DealerHand} > 21 then
		 if {HandValueSoft DealerHand} > 21 then
		    if {HandValueSoft PlayerHand} == 21 then 'blackjack'
		    else 'win' end
		 elseif {HandValueSoft DealerHand} < {HandValueSoft PlayerHand} then
		    if {HandValueHard PlayerHand} == 21 then 'blackjack'
		    else 'win' end
		 elseif {HandValueSoft DealerHand} == {HandValueSoft PlayerHand} then 'tie'
		 else 'loss' end
	      elseif {HandValueHard DealerHand} < {HandValueSoft PlayerHand} then
		 if {HandValueSoft PlayerHand} == 21 then 'blackjack'
		 else 'win' end
	      elseif {HandValueHard DealerHand} == {HandValueSoft PlayerHand} then 'tie'	 
	      else 'loss' end
	     else 'loss' end
end 

%Based on Game outcome we modify the players money
declare fun{GameEffectPlayer PlayerHand DealerHand Bet PlayerMoney}
	   if {GameOutcome PlayerHand DealerHand} == 'blackjack' then
	      PlayerMoney + Bet + Bet
	   elseif {GameOutcome PlayerHand DealerHand} == 'win' then
	      PlayerMoney + Bet
	   elseif {GameOutcome PlayerHand DealerHand} == 'loss' then
	      PlayerMoney - Bet
	   else
	      PlayerMoney
	   end
end

%Based on Game outcome we modify the dealers money
declare fun{GameEffectDealer PlayerHand DealerHand Bet DealerMoney}
	   if {GameOutcome PlayerHand DealerHand} == 'blackjack' then
	      DealerMoney - Bet - Bet
	   elseif {GameOutcome PlayerHand DealerHand} == 'win' then
	     DealerMoney - Bet
	   elseif {GameOutcome PlayerHand DealerHand} == 'loss' then
	      DealerMoney + Bet
	   else
	      DealerMoney
	   end
end

%We Move all the played cards to the out deck
declare fun {AddPlayerHandsToOut PlayerHands Index DeckOut}
	   %We add the cards to the top of the out of play deck going Player1Cards->Player2Cards->....->PlayerNCards
	   if Index > {List.length PlayerHands} then DeckOut
	   else
	      {AddPlayerHandsToOut PlayerHands Index+1 {Append {Nth PlayerHands Index} DeckOut}}
	   end     
end

%Add the Dealerhand on top of the outdeck
declare fun {MovePlayedOut PlayerHands DealerHand DeckOut}
	   {Append DealerHand {AddPlayerHandsToOut PlayerHands 1 DeckOut}}
end

%Create sublists given certain Indexes
declare fun {Sublist FullList ReturnList Index Start End }
   if Index==End then
      ReturnList
   else
      if Index<Start-1 then
	 {Sublist FullList ReturnList Index+1 Start End}
      else
	 {Sublist FullList {Append ReturnList [{Nth FullList Index+1}]} Index+1 Start End}
      end
   end
end

%Swap two values 
declare fun {Swap List Min Max Length}
	   {Append {Append {Append {Sublist List nil 0 1 Min-1} {Sublist List nil 1 Max Max}} {Append {Sublist List nil Min Min Max-1} {Sublist List nil 0 Min Min}}}{Sublist List nil Max Max Length}}
end

%Swap helper function to make prepare for swap
declare fun {SwapHelp Deck Index SwapPoint Length}
	   if Index == SwapPoint then Deck
	   elseif Index > SwapPoint then
	      {Swap Deck SwapPoint Index Length}
	   else
	      {Swap Deck Index SwapPoint Length}
	   end
end

%Function to Shuffle based on rand and swap
declare fun {Shuffler Deck Random Index Length}
	   if Index > Length then
	      {MakeShuffleRecord Random Deck}
	   else
	      {Shuffler {SwapHelp Deck Index {Random} mod {List.length Deck} + 1  {List.length Deck}} Random Index+1 Length}
	   end
end

% Outputs the amount of rounds the players stayed in for and how much money they have
declare fun{OutputPlayerMoneyRound PlayersMoney RoundsPlayed Index}
	   if Index > {List.length PlayersMoney} then ['])']
	   else
	      {Append {Append [{Nth RoundsPlayed Index}] ['#']} {Append [{Nth PlayersMoney Index}] {OutputPlayerMoneyRound PlayersMoney RoundsPlayed Index+1}}}
	   end
end

%output the final score
declare fun{OutputScore DealerMoney PlayersMoney RoundsPlayed}
	   {Append {Append ['(Game']  [DealerMoney]} {Append ['['] {OutputPlayerMoneyRound PlayersMoney RoundsPlayed 1}}}
end

% Function Checks wether any players are eligible to play for the game to continue
declare fun {CheckPlayersMoney PlayersMoney Index Bet}
	   if Index > {List.length PlayersMoney} then false
	   elseif {Nth PlayersMoney Index} >= Bet then true
	   else
	      {CheckPlayersMoney PlayersMoney Index+1 Bet}
	   end 	      
end

% Updates the list that keeps track of last round a player played
declare fun {UpdateRoundsPlayed PlayersMoney PlayersRoundsPlayed Index CurrentRound Bet}
	   if Index > {List.length PlayersMoney} then nil	 	      
	   elseif {Nth PlayersMoney Index} >= Bet then
	      {Append [CurrentRound] {UpdateRoundsPlayed PlayersMoney PlayersRoundsPlayed Index+1 CurrentRound Bet}}
	   else
	      {Append {Sublist PlayersRoundsPlayed nil 0 Index Index} {UpdateRoundsPlayed PlayersMoney PlayersRoundsPlayed Index+1 CurrentRound Bet}}
	   end
end 

%If the player doubled down we change their bet
declare fun {DoublePlayersBet PlayersBets Index ModifyNumber Modify}
	   if Index > {List.length PlayersBets} then nil
	   elseif Index == ModifyNumber then
	      {Append [Modify] {DoublePlayersBet PlayersBets Index+1 ModifyNumber Modify}}
	   else {Append [{Nth PlayersBets Index}] {DoublePlayersBet PlayersBets Index+1 ModifyNumber Modify}}
	   end
end

% Merge Modified PlayerHand to the array of playerhands
declare fun {MergeHands PlayersHands Hand Index Goal}
	   if Index > {List.length PlayersHands} then nil
	   elseif Index == Goal then
	      {Append [Hand] {MergeHands PlayersHands Hand Index+1 Goal}}
	   else
	      {Append [{Nth PlayersHands Index}] {MergeHands PlayersHands Hand Index+1 Goal}}
	   end
end

%Gives us a count of how many players are eligble for play
declare fun{HowManyPlayersIn PlayersMoney Bet Index Count}
	   if Index > {List.length PlayersMoney} then Count
	   elseif {Nth PlayersMoney Index} >= Bet then {HowManyPlayersIn PlayersMoney Bet Index+1 Count+1}
	   else
	      {HowManyPlayersIn PlayersMoney Bet Index+1 Count}
	   end 
end

%Makes array of players bets
declare fun {MakePlayersBet Players Index Bet}
	if Players < Index then nil
	else
	   {Append [Bet] {MakePlayersBet Players Index+1 Bet}}
	end
end

%Gives us an array of which players are in 1 if elligble 0 if not
declare fun{PlayersIn PlayersMoney Bet Index}
	   if Index > {List.length PlayersMoney} then nil
	   else
	      if {Nth PlayersMoney Index} >= Bet-1 then
		 {Append [1] {PlayersIn PlayersMoney Bet Index+1}}
	      else
		 {Append [0] {PlayersIn PlayersMoney Bet Index+1}}
	      end
	   end
end
declare fun{IndexToPlayerNumber PlayersMoney Bet Index}
	   if Index > {List.length PlayersMoney} then nil
	   elseif {Nth PlayersMoney Index} >= Bet-1 then
	      {Append [Index] {IndexToPlayerNumber PlayersMoney Bet Index+1}}
	   else
	      {IndexToPlayerNumber PlayersMoney Bet Index+1}
	   end
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


% Core Functions%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Evaluates the otcome Based on The cards and stuff. returns a record of table representation
declare fun {EvaluateOutcome Random DeckIn DeckOut PlayersMoney PlayersHands PlayersBet PlayersInvolved DealerHand DealerMoney Index NewPlayerMoney HandsIndex}
	   if Index > {List.length PlayersMoney} then
	      {MakeTableRepresentation DeckIn {MovePlayedOut PlayersHands DealerHand DeckOut} NewPlayerMoney DealerMoney Random}
	   elseif {Nth PlayersInvolved Index} == 1 then
	      {EvaluateOutcome Random DeckIn DeckOut PlayersMoney PlayersHands PlayersBet PlayersInvolved DealerHand {GameEffectDealer {Nth PlayersHands HandsIndex} DealerHand {Nth PlayersBet HandsIndex} DealerMoney} Index+1 {Append NewPlayerMoney [{GameEffectPlayer {Nth PlayersHands HandsIndex} DealerHand {Nth PlayersBet HandsIndex} {Nth PlayersMoney Index}}]} HandsIndex+1}
	   else
	      {EvaluateOutcome Random DeckIn DeckOut PlayersMoney PlayersHands PlayersBet PlayersInvolved DealerHand DealerMoney Index+1 {Append NewPlayerMoney [{Nth PlayersMoney Index}]} HandsIndex}
	    end	      
end

% Given a Player hand will produce the final output based on strategy mentione in assignment
declare fun{PlayPlayerHand Random DeckIn DeckOut Hand Bet Money}
	   if {List.length DeckIn} == 0 then
	      if {PlayerMove Hand Money Bet} ==  'stand' then
		 {MakeHandRecord Random DeckIn DeckOut Hand Bet}
	      else
		 local
		    Temp1 = {Shuffler DeckOut Random 1 {List.length DeckOut}}
		 in
		    {PlayPlayerHand Temp1.random Temp1.deckin nil Hand Bet Money}
		 end
	      end
	   elseif {PlayerMove Hand Money Bet} == 'hit' then
	      {PlayPlayerHand Random {Sublist DeckIn nil 0 2 {List.length DeckIn}} DeckOut {Append Hand [{Nth DeckIn 1}]} Bet Money}
	   elseif {PlayerMove Hand Money Bet} == 'double' then
	      if Money > 2*Bet then
		 {MakeHandRecord Random {Sublist DeckIn nil 0 2 {List.length DeckIn}} DeckOut {Append Hand [{Nth DeckIn 1}]} 2*Bet}
	      else
		 {PlayPlayerHand Random {Sublist DeckIn nil 0 2 {List.length DeckIn}} DeckOut {Append Hand [{Nth DeckIn 1}]} Bet Money}
	      end
	   else
	      {MakeHandRecord Random DeckIn DeckOut Hand Bet}
	   end
end

%Same as above but dealers cant double down
declare fun{PlayDealerHand Random DeckIn DeckOut Hand}
	   if {List.length DeckIn} == 0 then
	      if {PlayerMove Hand 0 0} == 'stand' then
		 {MakeHandRecord Random DeckIn DeckOut Hand nil}
	      else
		 local
		    Temp1 = {Shuffler DeckOut Random 1 {List.length DeckOut}}
		 in
		    {PlayDealerHand Temp1.random Temp1.deckin nil Hand}
		 end
	      end
	   elseif {PlayerMove Hand 0 0} == 'hit' then
	      {PlayDealerHand Random {Sublist DeckIn nil 0 2 {List.length DeckIn}} DeckOut {Append Hand [{Nth DeckIn 1}]}}
	   elseif {PlayerMove Hand 0 0} == 'double' then
	       {PlayDealerHand Random {Sublist DeckIn nil 0 2 {List.length DeckIn}} DeckOut {Append Hand [{Nth DeckIn 1}]}}
	   else
	      {MakeHandRecord Random DeckIn DeckOut Hand nil}
	   end
end

%Deal inital 2 cards to everyone in the deck
declare fun {DealInitalCards DeckIn DeckOut Players Index PlayerCards Random Lap}
	   if {List.length DeckIn} == 0 then
	      local
		 Temp = {Shuffler DeckOut Random 1 {List.length DeckOut}}
	      in
		 {DealInitalCards Temp.deckin nil Players Index PlayerCards Temp.random Lap}
	      end 
	   else
	      if Index == Players+2 then
		 if Lap == 2 then
		    {MakeGameRepresentation DeckIn DeckOut {Sublist PlayerCards nil 0 1 {List.length PlayerCards}-1} {Sublist PlayerCards nil 0 {List.length PlayerCards} {List.length PlayerCards}} Random}
		 else
		    {DealInitalCards DeckIn DeckOut Players 1 PlayerCards Random 2}
		 end
	      elseif Lap == 1 then
		 if Index == 1 then
		    {DealInitalCards {Sublist DeckIn nil 0 2 {List.length DeckIn}} DeckOut Players Index+1 [[{Nth DeckIn 1}]] Random Lap}
		 else
		    {DealInitalCards {Sublist DeckIn nil 0 2 {List.length DeckIn}} DeckOut Players Index+1 {Append PlayerCards [[{Nth DeckIn 1}]]} Random Lap}
		 end	 
	      else
		 {DealInitalCards {Sublist DeckIn nil 0 2 {List.length DeckIn}} DeckOut Players Index+1 {Append {Append {Sublist PlayerCards nil 0 1 Index-1} [{Append [{Nth {Nth PlayerCards Index} 1}] [{Nth DeckIn 1}]}]} {Sublist PlayerCards nil 0 Index+1 {List.length PlayerCards}}} Random Lap}
	      end
	   end      
end
% Plays all the hands in the roundwhen its done, we get a table evaluation      
declare fun{PlayHands Random DeckIn DeckOut PlayersMoney PlayersHands PlayersBet PlayersInvolved DealerHand DealerMoney Index IndexToPlayerNumber Bet}	   
	   if Index > {List.length PlayersHands} then
	      local
		 Temp = {PlayDealerHand Random DeckIn DeckOut {Nth DealerHand 1}}
	      in
		 {EvaluateOutcome Temp.random Temp.deckin Temp.deckout PlayersMoney PlayersHands PlayersBet {PlayersIn PlayersMoney Bet 1} Temp.hand DealerMoney 1 nil 1}
	      end
	   else
	      local
		  Temp = {PlayPlayerHand Random DeckIn DeckOut {Nth PlayersHands Index} {Nth PlayersBet Index} {Nth PlayersMoney {Nth IndexToPlayerNumber Index}}}
	       in
		 {PlayHands Temp.random Temp.deckin Temp.deckout PlayersMoney {MergeHands PlayersHands Temp.hand 1 Index} {DoublePlayersBet PlayersBet 1 Index Temp.bet}  [1 1 1] DealerHand DealerMoney Index+1 IndexToPlayerNumber Bet}
	      end 
	   end
end
%Play the game of blackjack	 
declare fun {PlayGame DeckIn DeckOut Random RoundsPlayed MaxRounds Bet DealerMoney PlayersMoney RoundsPlayersPlayed}
	   if RoundsPlayed == MaxRounds then {OutputScore DealerMoney PlayersMoney RoundsPlayersPlayed}
	   elseif {CheckPlayersMoney PlayersMoney 1 Bet} == false then {OutputScore DealerMoney PlayersMoney RoundsPlayersPlayed}
	   else
	      local
		 local
		    Temp2 = {DealInitalCards DeckIn DeckOut {HowManyPlayersIn PlayersMoney Bet 1 0} 1 nil Random 1}
		 in
		    Temp1 = {PlayHands Temp2.random Temp2.deckin Temp2.deckout PlayersMoney Temp2.playercards {MakePlayersBet {HowManyPlayersIn PlayersMoney Bet 1 0} 1 Bet} {PlayersIn PlayersMoney Bet 1} Temp2.dealerhand DealerMoney 1 {IndexToPlayerNumber PlayersMoney Bet 1} Bet}
		 end 
	      in
		 {PlayGame Temp1.deckin Temp1.deckout Temp1.random RoundsPlayed+1 MaxRounds Bet Temp1.dealermoney Temp1.playermoney {UpdateRoundsPlayed PlayersMoney RoundsPlayersPlayed 1 RoundsPlayed+1 Bet}}
	      end
	   end
end

declare fun {BlackJack Seed Players InitialMoney Bet MaxRounds} 
	   {NewRand Random Init _}
	   {Init Seed}
	   local
	      Temp1 = {Shuffler Deck Random 1 {List.length Deck}}
	   in
	       {PlayGame Temp1.deckin nil Temp1.random 0 MaxRounds Bet 0 {InitPlayerMoney Players 1 InitialMoney} {InitPlayerMoney Players 1 0}}
	   end
end  
{Browse {BlackJack 10 2 10 2 2}}
%{Browse {BlackJack 15 3 10 9 2}}
%{Browse {BlackJack 22 5 10 4 6}}
%{Browse {BlackJack 13 5 30 4 10}}
