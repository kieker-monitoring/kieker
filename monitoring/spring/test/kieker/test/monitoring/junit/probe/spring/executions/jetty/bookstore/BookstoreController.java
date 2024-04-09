/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.test.monitoring.junit.probe.spring.executions.jetty.bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Andre van Hoorn
 *
 * @since 1.6
 */
@Controller
@RequestMapping("/bookstore/*")
public final class BookstoreController {

	@Autowired
	private Bookstore bookstore;

	/**
	 * Default constructor.
	 */
	public BookstoreController() {
		// empty default constructor
	}

	/**
	 * Spring maps a specific web request onto this method. In this case it is the home path request.
	 *
	 * @return The home path.
	 */
	@RequestMapping(value = "/")
	public String home() {
		return "home";
	}

	/**
	 * Spring maps a specific web request onto this method. In this case it is the search path request.
	 *
	 * @param term
	 *            The term to search for.
	 * @param model
	 *            The Spring model object.
	 *
	 * @return The search path.
	 */
	@RequestMapping(value = "/search/{term}", method = RequestMethod.GET)
	public String search1(@PathVariable("term") final String term, final Model model) {
		model.addAttribute("result", this.bookstore.searchBook(term));
		return "search";
	}

	/**
	 * Spring maps a specific web request onto this method. In this case it is the search path request.
	 *
	 * @param term
	 *            The term to search for.
	 * @param model
	 *            The Spring model object.
	 *
	 * @return The search path.
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search2(@RequestParam final String term, final Model model) {
		model.addAttribute("result", this.bookstore.searchBook(term));
		return "search";
	}
}
