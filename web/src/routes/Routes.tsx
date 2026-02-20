import { BrowserRouter, Route, Routes } from "react-router-dom";
import SummaryPages from "../pages/summary/SummaryPages";
import AddTeamPage from './../pages/team/AddTeamPage';
import TeamListPage from "../pages/team/TeamListPage";
import EditTeamPage from "../pages/team/EditTeamPage";
import MatchListPage from "../pages/match/MatchListPage";
import AddMatchPage from "../pages/match/AddMatchPage";
import EditMatchPage from "../pages/match/EditMatchPage";

export default function AllRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<SummaryPages/> }/>

        <Route path="/summary" element={<SummaryPages/> }/>

          <Route path="/team/list"     element={<TeamListPage/> }/>
          <Route path="/team/add"      element={<AddTeamPage/> }/>
          <Route path="/team/edit/:id" element={<EditTeamPage/> }/>

          <Route path="/match/list"     element={<MatchListPage/> }/>
          <Route path="/match/add"      element={<AddMatchPage/> }/>
          <Route path="/match/edit/:id" element={<EditMatchPage/> }/>

          

      </Routes>
    </BrowserRouter>
  );
}