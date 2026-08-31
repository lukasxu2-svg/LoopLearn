import { Link } from "react-router-dom";
import "../styles/HomePage.css";

const learningTracks = [
  {
    number: "01",
    title: "Build the foundation",
    description:
      "Learn the language, tools, and habits that make every next step easier.",
    color: "coral",
  },
  {
    number: "02",
    title: "Ship real projects",
    description:
      "Turn concepts into portfolio pieces with guided, practical challenges.",
    color: "mint",
  },
  {
    number: "03",
    title: "Grow with momentum",
    description:
      "Keep your edge with new courses, expert patterns, and a clear path forward.",
    color: "yellow",
  },
];

function HomePage() {
  return (
    <main className="home-page">
      <nav className="home-nav" aria-label="Main navigation">
        <Link className="brand" to="/">
          <span className="brand-mark">&lt;/&gt;</span>
          <span>LoopLearn</span>
        </Link>
        <div className="auth-actions">
          <Link className="login-link" to="/login">
            Log in
          </Link>
          <Link className="register-button" to="/signup">
            Register <span aria-hidden="true">↗</span>
          </Link>
        </div>
      </nav>

      <section className="home-hero">
        <div className="hero-copy">
          <p className="eyebrow">
            <span /> A better way to learn code
          </p>
          <h1>
            Make something
            <br />
            <em>worth sharing.</em>
          </h1>
          <p className="hero-description">
            A focused subscription for curious developers. Learn programming
            through clear courses, hands-on projects, and a path that keeps
            moving.
          </p>
          <div className="hero-actions">
            <Link className="primary-action" to="/signup">
              Start learning <span aria-hidden="true">↗</span>
            </Link>
            <a className="text-action" href="#tracks">
              See how it works <span aria-hidden="true">↓</span>
            </a>
          </div>
          <div className="hero-note">
            <span className="avatar-stack" aria-hidden="true">
              <i />
              <i />
              <i />
            </span>
            <span>
              <strong>Learn at your pace.</strong> Cancel anytime.
            </span>
          </div>
        </div>

        <div
          className="hero-visual"
          aria-label="Example coding lesson dashboard"
        >
          <div className="visual-sticker">
            LESSON
            <br />
            <strong>01</strong>
          </div>
          <div className="code-window">
            <div className="window-bar">
              <span />
              <span />
              <span />
              <small>hello-world.js</small>
            </div>
            <div className="code-content">
              <p>
                <b>01</b> <span className="keyword">const</span>{" "}
                <span className="variable">path</span> = [
              </p>
              <p>
                <b>02</b> &nbsp;{" "}
                <span className="string">&quot;curiosity&quot;</span>,
              </p>
              <p>
                <b>03</b> &nbsp;{" "}
                <span className="string">&quot;practice&quot;</span>,
              </p>
              <p>
                <b>04</b> &nbsp;{" "}
                <span className="string">&quot;momentum&quot;</span>
              </p>
              <p>
                <b>05</b> ];
              </p>
              <p>
                <b>06</b>
              </p>
              <p>
                <b>07</b> <span className="keyword">return</span>{" "}
                <span className="function">path</span>
                <span className="plain">.</span>
                <span className="function">map</span>(step);
              </p>
              <p className="code-cursor">
                <b>08</b> <span>_</span>
              </p>
            </div>
            <div className="window-footer">
              <span className="status-dot" /> Your next project starts here{" "}
              <span>↗</span>
            </div>
          </div>
          <div className="visual-caption">
            <span>FROM IDEA</span>
            <strong>TO IMPACT</strong>
          </div>
        </div>
      </section>

      <section className="trust-row" aria-label="Learning platform highlights">
        <span>ONE SUBSCRIPTION</span>
        <span className="trust-line" />
        <strong>Courses that compound</strong>
        <span className="trust-line" />
        <span>YOUR PACE · YOUR PATH</span>
      </section>

      <section className="tracks-section" id="tracks">
        <div className="section-heading">
          <p className="eyebrow">
            <span /> The LoopLearn method
          </p>
          <h2>
            Small steps.
            <br />
            <em>Serious progress.</em>
          </h2>
        </div>
        <div className="track-list">
          {learningTracks.map((track) => (
            <article className={`track-card ${track.color}`} key={track.number}>
              <span className="track-number">{track.number}</span>
              <div>
                <h3>{track.title}</h3>
                <p>{track.description}</p>
              </div>
              <span className="track-arrow" aria-hidden="true">
                ↗
              </span>
            </article>
          ))}
        </div>
      </section>

      <footer className="home-footer">
        <span>
          LOOP<span className="footer-accent">LEARN</span>
        </span>
        <span>Programming, in progress.</span>
      </footer>
    </main>
  );
}

export default HomePage;
